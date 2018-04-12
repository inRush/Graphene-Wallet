package cy.agorise.graphenej.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.ApiCall;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.models.WitnessResponse;
import cy.agorise.graphenej.objects.Memo;
import cy.agorise.graphenej.operations.TransferOperation;

/**
 * Class used to encapsulate the communication sequence used to retrieve the transaction history of
 * a given user.
 */
public class GetRelativeAccountHistory extends BaseGrapheneHandler {
    // Sequence of message ids
    private final static int LOGIN_ID = 1;
    private final static int GET_HISTORY_ID = 2;
    private final static int GET_HISTORY_DATA = 3;

    // Default value constants
    public static final int DEFAULT_STOP = 0;
    public static final int DEFAULT_START = 0;
    public static final int MAX_LIMIT = 100;

    // API call parameters
    private UserAccount mUserAccount;
    private int stop;
    private int limit;
    private int start;
    private WitnessResponseListener mListener;
    private WebSocket mWebsocket;

    private int currentId = 1;
    private int apiId = -1;

    private boolean mOneTime;

    /**
     * Constructor that takes all possible parameters.
     *
     * @param userAccount   The user account to be queried
     * @param stop          Sequence number of earliest operation
     * @param limit         Maximum number of operations to retrieve (must not exceed 100)
     * @param start         Sequence number of the most recent operation to retrieve
     * @param oneTime       boolean value indicating if WebSocket must be closed (true) or not
     *                      (false) after the response
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetRelativeAccountHistory(UserAccount userAccount, int stop, int limit, int start, boolean oneTime, WitnessResponseListener listener){
        super(listener);
        if(limit > MAX_LIMIT) limit = MAX_LIMIT;
        this.mUserAccount = userAccount;
        this.stop = stop;
        this.limit = limit;
        this.start = start;
        this.mOneTime = oneTime;
        this.mListener = listener;
    }

    /**
     * Constructor that uses the default values, and sets the limit to its maximum possible value.
     *
     * @param userAccount   The user account to be queried
     * @param oneTime       boolean value indicating if WebSocket must be closed (true) or not
     *                      (false) after the response
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetRelativeAccountHistory(UserAccount userAccount, boolean oneTime, WitnessResponseListener listener){
        super(listener);
        this.mUserAccount = userAccount;
        this.stop = DEFAULT_STOP;
        this.limit = MAX_LIMIT;
        this.start = DEFAULT_START;
        this.mOneTime = oneTime;
        this.mListener = listener;
    }

    /**
     * Constructor that takes all possible parameters for the query.
     * Using this constructor the WebSocket connection closes after the response.
     *
     * @param userAccount   The user account to be queried
     * @param stop          Sequence number of earliest operation
     * @param limit         Maximum number of operations to retrieve (must not exceed 100)
     * @param start         Sequence number of the most recent operation to retrieve
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetRelativeAccountHistory(UserAccount userAccount, int stop, int limit, int start, WitnessResponseListener listener){
        this(userAccount, stop, limit, start, true, listener);
    }

    /**
     * Constructor that uses the default values, and sets the limit to its maximum possible value.
     * Using this constructor the WebSocket connection closes after the response.
     *
     * @param userAccount   The user account to be queried
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetRelativeAccountHistory(UserAccount userAccount, WitnessResponseListener listener){
        this(userAccount, true, listener);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        mWebsocket = websocket;
        ArrayList<Serializable> loginParams = new ArrayList<>();
        loginParams.add(null);
        loginParams.add(null);
        ApiCall loginCall = new ApiCall(1, RPC.CALL_LOGIN, loginParams, RPC.VERSION, currentId);
        websocket.sendText(loginCall.toJsonString());
    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        String response = frame.getPayloadText();
        System.out.println("<<< "+response);
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
        if(baseResponse.error != null){
            mListener.onError(baseResponse.error);
            if(mOneTime){
                websocket.disconnect();
            }
        }else{
            currentId++;
            ArrayList<Serializable> emptyParams = new ArrayList<>();
            if(baseResponse.id == LOGIN_ID){
                ApiCall getRelativeAccountHistoryId = new ApiCall(1, RPC.CALL_HISTORY, emptyParams, RPC.VERSION, currentId);
                websocket.sendText(getRelativeAccountHistoryId.toJsonString());
            }else if(baseResponse.id == GET_HISTORY_ID){
                Type ApiIdResponse = new TypeToken<WitnessResponse<Integer>>() {}.getType();
                WitnessResponse<Integer> witnessResponse = gson.fromJson(response, ApiIdResponse);
                apiId = witnessResponse.result.intValue();

                sendRelativeAccountHistoryRequest();
            }else if(baseResponse.id >= GET_HISTORY_DATA){
                Type RelativeAccountHistoryResponse = new TypeToken<WitnessResponse<List<HistoricalTransfer>>>(){}.getType();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(TransferOperation.class, new TransferOperation.TransferDeserializer());
                gsonBuilder.registerTypeAdapter(AssetAmount.class, new AssetAmount.AssetAmountDeserializer());
                gsonBuilder.registerTypeAdapter(Memo.class, new Memo.MemoDeserializer());
                WitnessResponse<List<HistoricalTransfer>> transfersResponse = gsonBuilder.create().fromJson(response, RelativeAccountHistoryResponse);
                mListener.onSuccess(transfersResponse);
            }
        }
    }

    /**
     * Sends the actual get_relative_account_history request.
     */
    private void sendRelativeAccountHistoryRequest(){
        ArrayList<Serializable> params = new ArrayList<>();
        params.add(mUserAccount.getObjectId());
        params.add(this.stop);
        params.add(this.limit);
        params.add(this.start);

        ApiCall getRelativeAccountHistoryCall = new ApiCall(apiId, RPC.CALL_GET_RELATIVE_ACCOUNT_HISTORY, params, RPC.VERSION, currentId);
        mWebsocket.sendText(getRelativeAccountHistoryCall.toJsonString());
    }

    /**
     * Updates the arguments and makes a new call to the get_relative_account_history API.
     *
     * @param stop Sequence number of earliest operation
     * @param limit Maximum number of operations to retrieve (must not exceed 100)
     * @param start Sequence number of the most recent operation to retrieve
     */
    public void retry(int stop, int limit, int start){
        this.stop = stop;
        this.limit = limit;
        this.start = start;
        sendRelativeAccountHistoryRequest();
    }

    /**
     * Disconnects the WebSocket.
     */
    public void disconnect(){
        if(mWebsocket != null && mWebsocket.isOpen() && mOneTime){
            mWebsocket.disconnect();
        }
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        if(frame.isTextFrame())
            System.out.println(">>> "+frame.getPayloadText());
    }
}
