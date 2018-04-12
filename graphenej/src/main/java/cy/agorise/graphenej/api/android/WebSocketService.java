package cy.agorise.graphenej.api.android;

import android.os.Handler;

import com.google.gson.Gson;
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
import cy.agorise.graphenej.models.ApiCall;
import cy.agorise.graphenej.models.ApiCode;
import cy.agorise.graphenej.models.BaseResponse;
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
         * 正在连接诶
         */
        CONNECTING
    }

    public ApiCode apiCode;
    private WebSocket mWebSocket;
    private WebSocketFactory factory;
    private static WebSocketService mWebSocketService;
    private HashMap<Long, OnResponseListener> mCallbackMap = new HashMap<>();
    private Gson gson = new Gson();
    private Handler mHandler = new Handler();
    private String mUrl;

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

    public static WebSocketService getInstance() {
        if (mWebSocketService == null) {
            synchronized (WebSocketService.class) {
                if (mWebSocketService == null) {
                    mWebSocketService = new WebSocketService();
                }
            }
        }
        return mWebSocketService;
    }

    private WebSocketService() {

        try {
            apiCode = new ApiCode();
            SSLContext context = NaiveSSLContext.getInstance("TLS");
            factory = new WebSocketFactory();
            factory.setSSLContext(context);
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

    private void reconnect() {
        init(mUrl);
        mHandler.postDelayed(mConnectTask, 3000);
    }

    public void connect(String url) throws ExceptionInInitializerError {
        init(url);
        mHandler.post(mConnectTask);
    }


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
        if (this.mStatus != WsStatus.CONNECT_SUCCESS) {
            throw new Exception("socket is not connected");
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

    public void disconnect() {
        mWebSocket.disconnect();
    }


    private Runnable mConnectTask = new Runnable() {
        @Override
        public void run() {
            setStatus(WsStatus.CONNECTING);
            mWebSocket.connectAsynchronously();
        }
    };

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

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
            super.onConnectError(websocket, exception);
            setStatus(WsStatus.CONNECT_FAIL);
            reconnect();
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            setStatus(WsStatus.CONNECT_FAIL);
            reconnect();
        }
    }
}
