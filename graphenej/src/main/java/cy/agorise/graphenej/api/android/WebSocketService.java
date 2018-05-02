package cy.agorise.graphenej.api.android;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.api.android.util.NetUtil;
import cy.agorise.graphenej.models.ApiCall;
import cy.agorise.graphenej.models.ApiCode;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.ChainProperties;
import cy.agorise.graphenej.models.WitnessResponse;
import cy.agorise.graphenej.test.NaiveSSLContext;

/**
 * @author inrush
 * @date 2018/3/31.
 */

public class WebSocketService {

    private static final int LOGIN_ID = 1;
    private static final int DATABASE_ID = 2;
    private static final int NETWORK_BROADCAST_ID = 3;
    private static final int HISTORY_ID = 4;
    private static final int CRYPTO_ID = 5;
    private static final int CHAIN_ID = 6;
    /**
     * 缓存的回调ID下限
     */
    private static final long LOW_ID = 10L;
    /**
     * 缓存的回调ID上限
     */
    private static final long HIGH_ID = 200L;
    private long mCurrentId = LOW_ID;
    private WsStatus mStatus;

    public String getChainId() {
        return mChainId;
    }

    public enum WsStatus {
        /**
         * 连接成功
         */
        CONNECT_SUCCESS,
        /**
         * 连接失败
         */
        CONNECT_FAIL,
        /**
         * 正在连接
         */
        CONNECTING,
        /**
         * 连接断开
         */
        DISCONNECT,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 处于初始化状态
         */
        CONNECTION_INITIALIZED,
        /**
         * 初始化成功
         */
        CONNECTION_INITIALIZED_SUCCESS
    }

    public static final String ACTION_COMPLETE = "WebSocketService.complete";
    public static final String ACTION_DISCONNECTED = "WebSocketService.disconnected";
    public static final String ACTION_CONNECT_TIMEOUT = "WebSocketService.timeout";
    public static final String ACTION_CONNECT_ERROR = "WebSocketService.error";
    public static final String SOCKET_TAG = "WebSocketService.which.service";

    public ApiCode apiCode;
    private String mChainId;
    private WebSocket mWebSocket;
    private WebSocketFactory factory;
    private HashMap<Long, OnResponseListener> mCallbackMap = new HashMap<>();
    private Gson gson = new Gson();
    private Handler mHandler = new Handler();
    private String mUrl;

    private String mSocketTag;
    private Context mContext;
    /**
     * 是否主动断开连接
     */
    private boolean isActiveDisconnect = false;
    private int mRetryCount = 3;
    private int mRetryDelay = 3000;
    private int mRetryIndex = 0;
    /**
     * 设置15秒超时
     */
    private int mTimeout = 15000;

    public interface OnResponseListener {
        /**
         * 成功回调
         *
         * @param result 回调结果
         */
        void onSuccess(String result);

        /**
         * 失败回调
         *
         * @param error 错误
         */
        void onError(BaseResponse.Error error);
    }


    public WebSocketService(Context context) {
        mContext = context;
        mRetryIndex = 0;
        this.mStatus = WsStatus.CONNECTION_INITIALIZED;
        try {
            apiCode = new ApiCode();
            SSLContext sslContext = NaiveSSLContext.getInstance("TLS");
            factory = new WebSocketFactory();
            factory.setSSLContext(sslContext);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void init(String url) throws ExceptionInInitializerError {
        mUrl = url;
        try {
            mWebSocket = factory.createSocket(url);
            mWebSocket.addListener(new ResponseAdapter());
        } catch (IOException e) {
            throw new ExceptionInInitializerError("WebSocket Initial Error");
        }
    }

    public void resetRetryIndex() {
        mRetryIndex = 0;
    }

    public void reconnect() {
        if (!NetUtil.isNetworkConnected(mContext)) {
            connectionFailure();
            removeTimeoutTask();
        } else if (this.mStatus == WsStatus.CONNECT_FAIL && ++mRetryIndex > mRetryCount) {
            setStatus(WsStatus.CONNECT_TIMEOUT);
            sendBroadcast(ACTION_CONNECT_TIMEOUT);
            removeTimeoutTask();
        } else if (this.mStatus == WsStatus.CONNECT_FAIL
                || this.mStatus == WsStatus.CONNECT_TIMEOUT
                || this.mStatus == WsStatus.DISCONNECT) {
            init(mUrl);
            mHandler.postDelayed(mConnectTask, mRetryDelay);
        }
    }


    public void connect(String url, String tag) throws ExceptionInInitializerError {
        mSocketTag = tag;
        init(url);
        mHandler.post(mConnectTask);
    }

    private void connectionComplete() {
        setStatus(WsStatus.CONNECTION_INITIALIZED_SUCCESS);
        sendBroadcast(ACTION_COMPLETE);
        removeTimeoutTask();
    }

    private void connectionFailure() {
        setStatus(WsStatus.CONNECT_FAIL);
        sendBroadcast(ACTION_CONNECT_ERROR);
        reconnect();
    }

    private void connectionDisconncet() {
        setStatus(WsStatus.DISCONNECT);

        sendBroadcast(ACTION_DISCONNECTED);
        if (!isActiveDisconnect) {
            reconnect();
        }
    }

    /**
     * 提交超时任务,防止长时间未连接超时
     */
    private void postTimeoutTask() {
        mHandler.postDelayed(mTimeoutTask, mTimeout);
    }

    private void removeTimeoutTask() {
        mHandler.removeCallbacks(mTimeoutTask);
    }

    private Runnable mTimeoutTask = new Runnable() {
        @Override
        public void run() {
            setStatus(WsStatus.CONNECT_TIMEOUT);
            sendBroadcast(ACTION_CONNECT_TIMEOUT);
            // 在重连延迟中,终止延迟连接任务
            mHandler.removeCallbacks(mConnectTask);
            // 在重连中,断开重连
            if (mStatus == WsStatus.CONNECTING) {
                disconnect();
            }
        }
    };
    private Runnable mConnectTask = new Runnable() {
        @Override
        public void run() {
            setStatus(WsStatus.CONNECTING);
            mWebSocket.connectAsynchronously();
            postTimeoutTask();
        }
    };

    /**
     * 生成回调ID，防止Map中的回调过多
     *
     * @return Long
     */
    public long generateId() {
        if (mCurrentId > HIGH_ID) {
            mCurrentId = LOW_ID;
        }
        return mCurrentId++;
    }

    public void call(ApiCall apiCall, OnResponseListener listener) throws Exception {
        if (this.mStatus != WsStatus.CONNECTION_INITIALIZED_SUCCESS) {
            throw new Exception("socket is not initialized");
        }
        try {
            mWebSocket.sendText(apiCall.toJsonString());
            mCallbackMap.put(apiCall.sequenceId, listener);
        } catch (Exception e) {
            reconnect();
        }
    }


    private void setStatus(WsStatus status) {
        this.mStatus = status;
    }

    public WsStatus getStatus() {
        return this.mStatus;
    }

    public String getSocketTag() {
        return mSocketTag;
    }


    public void setRetryCount(int retryCount) {
        this.mRetryCount = retryCount;
    }

    public void setRetryDelay(int retryDelay) {
        this.mRetryDelay = retryDelay;
    }

    public boolean isConnected() {
        return mStatus == WsStatus.CONNECTION_INITIALIZED_SUCCESS;
    }

    public boolean isInitializedComplete() {
        return mStatus == WsStatus.CONNECTION_INITIALIZED_SUCCESS;
    }

    public void disconnect() {
        isActiveDisconnect = true;
        mWebSocket.disconnect();
    }


    private void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(SOCKET_TAG, mSocketTag);
        mContext.sendBroadcast(intent);
    }

    class ResponseAdapter extends WebSocketAdapter {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            setStatus(WsStatus.CONNECT_SUCCESS);
            ArrayList<Serializable> loginParams = new ArrayList<>();
            loginParams.add(null);
            loginParams.add(null);
            ApiCall loginCall = new ApiCall(1, RPC.CALL_LOGIN, loginParams, RPC.VERSION, LOGIN_ID);
            websocket.sendText(loginCall.toJsonString());
        }

        @Override
        public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            String response = frame.getPayloadText();
            BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
            if (baseResponse.error != null) {
                if (baseResponse.id >= LOW_ID) {
                    mCallbackMap.get(baseResponse.id).onError(baseResponse.error);
                }
            } else if (baseResponse.id == LOGIN_ID) {
                sendCall(websocket, RPC.CALL_DATABASE, DATABASE_ID);
            } else if (baseResponse.id == DATABASE_ID) {
                apiCode.setDataseId(getId(response));
                sendCall(websocket, RPC.CALL_NETWORK_BROADCAST, NETWORK_BROADCAST_ID);
            } else if (baseResponse.id == NETWORK_BROADCAST_ID) {
                apiCode.setNetworkBroadcastId(getId(response));
                sendCall(websocket, RPC.CALL_HISTORY, HISTORY_ID);
            } else if (baseResponse.id == HISTORY_ID) {
                apiCode.setHistoryId(getId(response));
                sendCall(websocket, RPC.CALL_CRYPTO, CRYPTO_ID);
            } else if (baseResponse.id == CRYPTO_ID) {
                apiCode.setCryptoId(getId(response));
                sendGetChainPropertiesCall(websocket);
            } else if (baseResponse.id == CHAIN_ID) {
                setChainId(response);
                connectionComplete();
            } else {
                mCallbackMap.get(baseResponse.id).onSuccess(response);
            }
        }


        private int getId(String response) {
            Type apiIdResponse = new TypeToken<WitnessResponse<Integer>>() {
            }.getType();
            WitnessResponse<Integer> witnessResponse = gson.fromJson(response, apiIdResponse);
            return witnessResponse.result;
        }


        private void sendCall(WebSocket webSocket, String apiName, int nextId) {
            ApiCall getDatabaseId = new ApiCall(1, apiName, new ArrayList<Serializable>(), RPC.VERSION, nextId);
            webSocket.sendText(getDatabaseId.toJsonString());
        }

        private void sendGetChainPropertiesCall(WebSocket webSocket) {
            ArrayList<Serializable> params = new ArrayList<>();
            ArrayList<Serializable> ids = new ArrayList<>();
            ids.add(ChainProperties.OBJECT_ID);
            params.add(ids);
            ApiCall call = new ApiCall(DATABASE_ID, RPC.GET_OBJECTS, params, RPC.VERSION, CHAIN_ID);
            webSocket.sendText(call.toJsonString());
        }

        private void setChainId(String response) {
            Type apiIdResponse = new TypeToken<WitnessResponse<List<ChainProperties>>>() {
            }.getType();
            WitnessResponse<List<ChainProperties>> witnessResponse = new GsonBuilder()
                    .registerTypeAdapter(ChainProperties.ImmutableParameters.class,
                            new ChainProperties.ImmutableParameters.ImmutableParametersDeserializer()).create()
                    .fromJson(response, apiIdResponse);
            mChainId = witnessResponse.result.get(0).getChain_id();
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
            connectionFailure();
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            connectionDisconncet();
        }
    }
}
