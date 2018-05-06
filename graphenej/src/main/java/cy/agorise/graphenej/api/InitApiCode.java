package cy.agorise.graphenej.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.ApiCall;
import cy.agorise.graphenej.models.ApiCode;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 * @author inrush
 * @date 2018/4/4.
 */

public class InitApiCode extends BaseGrapheneHandler {

    private static final int LOGIN_ID = 1;
    private static final int DATABASE_ID = 2;
    private static final int NETWORK_BROADCAST_ID = 3;
    private static final int HISTORY_ID = 4;
    private static final int CRYPTO_ID = 5;

    private Gson gson = new Gson();
    private ApiCode mApiCode = new ApiCode();

    public InitApiCode(WitnessResponseListener listener) {
        super(listener);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
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
            mListener.onError(baseResponse.error);
        } else if (baseResponse.id == LOGIN_ID) {
            sendCall(websocket, RPC.CALL_DATABASE, DATABASE_ID);
        } else if (baseResponse.id == DATABASE_ID) {
            mApiCode.setDatabaseId(getId(response));
            sendCall(websocket, RPC.CALL_NETWORK_BROADCAST, NETWORK_BROADCAST_ID);
        } else if (baseResponse.id == NETWORK_BROADCAST_ID) {
            mApiCode.setNetworkBroadcastId(getId(response));
            sendCall(websocket, RPC.CALL_HISTORY, HISTORY_ID);
        } else if (baseResponse.id == HISTORY_ID) {
            mApiCode.setHistoryId(getId(response));
            sendCall(websocket, RPC.CALL_CRYPTO, CRYPTO_ID);
        } else if (baseResponse.id == CRYPTO_ID) {
            mApiCode.setCryptoId(getId(response));
            WitnessResponse<ApiCode> apiCodeWitnessResponse = new WitnessResponse<>();
            apiCodeWitnessResponse.result = mApiCode;
            mListener.onSuccess(apiCodeWitnessResponse);
        }

    }

    private int getId(String response) {
        Type ApiIdResponse = new TypeToken<WitnessResponse<Integer>>() {
        }.getType();
        WitnessResponse<Integer> witnessResponse = gson.fromJson(response, ApiIdResponse);
        return witnessResponse.result;
    }

    private void sendCall(WebSocket webSocket, String apiName, int nextId) {
        ApiCall getDatabaseId = new ApiCall(1, RPC.CALL_NETWORK_BROADCAST, new ArrayList<Serializable>(), RPC.VERSION, nextId);
        webSocket.sendText(getDatabaseId.toJsonString());
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        mListener.onError(new BaseResponse.Error(cause.getMessage()));
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        mListener.onError(new BaseResponse.Error(cause.getMessage()));
    }
}
