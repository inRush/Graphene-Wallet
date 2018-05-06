package com.gxb.gxswallet.services.rpc;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.manager.AssetManager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cy.agorise.graphenej.api.BaseRpcHandler;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.api.android.util.NetUtil;

/**
 * @author inrush
 * @date 2018/4/16.
 */

public class WebSocketServicePool {
    @SuppressLint("StaticFieldLeak")
    private static WebSocketServicePool sServicePool;
    private LinkedHashMap<String, WebSocketService> mWebSocketServiceMap;
    private HashMap<String, Status> mWebSocketServiceCompleteMap;
    private List<WebSocketService> mWebSocketServices;
    private Context mContext;

    private OnAddConnectListener mOnAddConnectListener;
    private OnDisconnectListener mOnDisconnectListener;
    private String mAddConnectName;
    private String mDisconnectName;

    public enum Status {
        /**
         * 连接完成(包括初始化)
         */
        CONNECTION_COMPLETE,
        /**
         * 连接失败
         */
        CONNECTION_FAIL,
        /**
         * 连接超时
         */
        CONNECTION_TIMEOUT,
        /**
         * 连接断开
         */
        CONNECTION_DISCONNECTED,
        /**
         * 连接未开始,未进行初始化
         */
        CONNECTION_UNINITIALIZED
    }

    public static WebSocketServicePool getInstance() {
        if (sServicePool == null) {
            synchronized (WebSocketServicePool.class) {
                if (sServicePool == null) {
                    sServicePool = new WebSocketServicePool();
                }
            }
        }
        return sServicePool;
    }

    private WebSocketServicePool() {
        mWebSocketServiceMap = new LinkedHashMap<>();
        mWebSocketServiceCompleteMap = new HashMap<>();
        mWebSocketServices = new ArrayList<>();
    }

    private void connect(String assetName, String url) {
        WebSocketService service = new WebSocketService(mContext);
        service.connect(url, assetName);
        mWebSocketServiceMap.put(assetName, service);
        mWebSocketServiceCompleteMap.put(assetName, Status.CONNECTION_UNINITIALIZED);
        mWebSocketServices.add(service);
    }

    /**
     * 添加连接监听器
     */
    public interface OnAddConnectListener {
        /**
         * 连接成功
         */
        void onConnectedSuccess();

        /**
         * 连接失败
         */
        void onConnectedFailure();
    }

    /**
     * 取消连接监听器
     */
    public interface OnDisconnectListener {
        /**
         * 连接成功
         */
        void onDisconnectedSuccess();
    }



    public synchronized void addConnect(String assetName, String url, OnAddConnectListener listener) {
        mOnAddConnectListener = listener;
        mAddConnectName = assetName;
        connect(assetName, url);
    }

    public synchronized void disconnect(String assetName, OnDisconnectListener listener) {
        mOnDisconnectListener = listener;
        mDisconnectName = assetName;
        WebSocketService service = mWebSocketServiceMap.get(assetName);
        mWebSocketServiceMap.remove(assetName);
        mWebSocketServiceCompleteMap.remove(assetName);
        mWebSocketServices.remove(service);
        if (service.isConnected()) {
            service.disconnect();
        }
    }

    public WebSocketService getService(String assetName) {
        if (!mWebSocketServiceCompleteMap.get(assetName).equals(Status.CONNECTION_COMPLETE)) {
            mWebSocketServiceMap.get(assetName).reconnect();
            return null;
        }
        return mWebSocketServiceMap.get(assetName);
    }


    /**
     * 生成任务队列(不同区块链的参数一致时使用这个,一致时使用下面的方法)
     *
     * @param handlerClass 接口Class
     * @param argsClass    参数Class
     * @param args         参数
     * @return RpcTask[]
     */
    public RpcTask[] generateTasks(Class<? extends BaseRpcHandler> handlerClass, Class[] argsClass, Object[] args) {
        try {
            RpcTask[] rpcTasks = new RpcTask[mWebSocketServiceMap.size()];
            int index = 0;
            for (Map.Entry<String, WebSocketService> entry : mWebSocketServiceMap.entrySet()) {
                RpcTask rpcTask = generateTask(entry.getKey(), entry.getValue(), handlerClass, argsClass, args);
                rpcTasks[index++] = rpcTask;
            }
            return rpcTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成任务队列(不同区块链的参数不一致时使用这个,一致时使用上面的方法)
     *
     * @param handlerClass 接口Class
     * @param argsClass    参数Class
     * @param args         参数
     * @return RpcTask[]
     */
    public RpcTask[] generateTasks(Class<? extends BaseRpcHandler> handlerClass, Class[] argsClass, List<Object[]> args) {
        try {
            RpcTask[] rpcTasks = new RpcTask[mWebSocketServiceMap.size()];
            int index = 0;
            for (Map.Entry<String, WebSocketService> entry : mWebSocketServiceMap.entrySet()) {
                RpcTask rpcTask = generateTask(entry.getKey(), entry.getValue(), handlerClass, argsClass, args.get(index));
                rpcTasks[index++] = rpcTask;
            }
            return rpcTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private RpcTask generateTask(String serverTag, WebSocketService service,
                                 Class<? extends BaseRpcHandler> handlerClass, Class[] argsClass, Object[] args) throws Exception {
        Class[] argsCls = new Class[argsClass.length + 1];
        argsCls[0] = WebSocketService.class;
        System.arraycopy(argsClass, 0, argsCls, 1, argsClass.length);
        Constructor c = handlerClass.getDeclaredConstructor(argsCls);
        Object[] as = new Object[args.length + 1];
        as[0] = service;
        System.arraycopy(args, 0, as, 1, args.length);
        BaseRpcHandler handler = (BaseRpcHandler) c.newInstance(as);
        return new RpcTask(handler, serverTag);

    }

    public void initPool(Application context) {
        mContext = context;
        registerReceiver();
        List<AssetData> assets = AssetManager.getInstance().getEnableList();
        for (AssetData asset : assets) {
            connect(asset.getName(), asset.getNets().get(0));
        }
    }


    /**
     * 检查是否全部服务连接完成
     *
     * @return Boolean
     */
    public boolean checkAllServiceConnectSuccess() {
        for (Map.Entry<String, Status> entry : mWebSocketServiceCompleteMap.entrySet()) {
            if (!entry.getValue().equals(Status.CONNECTION_COMPLETE)) {
                return false;
            }
        }
        return true;
    }

    public List<WebSocketService> getServices() {
        return mWebSocketServices;
    }

    /**
     * 注册WebSocket初始化完成接收者
     */
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WebSocketService.ACTION_COMPLETE);
        intentFilter.addAction(WebSocketService.ACTION_CONNECT_ERROR);
        intentFilter.addAction(WebSocketService.ACTION_DISCONNECTED);
        intentFilter.addAction(WebSocketService.ACTION_CONNECT_TIMEOUT);
        mContext.registerReceiver(new WebSocketReceiver(), intentFilter);

        IntentFilter connectionIntentFilter = new IntentFilter();
        connectionIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(new ConnectionChangeReceiver(), connectionIntentFilter);
    }

    class WebSocketReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            String assetName = intent.getStringExtra(WebSocketService.SOCKET_TAG);
            switch (action) {
                case WebSocketService.ACTION_COMPLETE:
                    mWebSocketServiceCompleteMap.put(assetName, Status.CONNECTION_COMPLETE);
                    if (Objects.equals(mAddConnectName, assetName) && mOnAddConnectListener != null) {
                        mAddConnectName = null;
                        mOnAddConnectListener.onConnectedSuccess();
                        mOnAddConnectListener = null;
                    }
                    break;
                case WebSocketService.ACTION_CONNECT_ERROR:
                    mWebSocketServiceCompleteMap.put(assetName, Status.CONNECTION_FAIL);
                    break;
                case WebSocketService.ACTION_DISCONNECTED:
                    mWebSocketServiceCompleteMap.put(assetName, Status.CONNECTION_DISCONNECTED);
                    if (Objects.equals(mDisconnectName, assetName) && mOnDisconnectListener != null) {
                        mDisconnectName = null;
                        mOnDisconnectListener.onDisconnectedSuccess();
                        mOnDisconnectListener = null;
                    }
                    break;
                case WebSocketService.ACTION_CONNECT_TIMEOUT:
                    mWebSocketServiceCompleteMap.put(assetName, Status.CONNECTION_TIMEOUT);
                    App.showToast(R.string.connection_connect_failure);
                    if (Objects.equals(mAddConnectName, assetName) && mOnAddConnectListener != null) {
                        mAddConnectName = null;
                        mOnAddConnectListener.onConnectedFailure();
                        mOnAddConnectListener = null;
                        disconnect(assetName, null);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetUtil.isNetworkConnected(context)) {
                for (WebSocketService service : mWebSocketServices) {
                    service.resetRetryIndex();
                    service.reconnect();
                }
            }
        }
    }
}
