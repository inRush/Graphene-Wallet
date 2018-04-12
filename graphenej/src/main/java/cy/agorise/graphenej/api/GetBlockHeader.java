package cy.agorise.graphenej.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.ApiCall;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.BlockHeader;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 *  Class that implements get_block_header request handler.
 *
 *  Retrieve a block header.
 *
 *  The request returns the header of the referenced block, or null if no matching block was found
 *
 *  @see <a href="https://goo.gl/qw1eeb">get_block_header API doc</a>
 */
public class GetBlockHeader extends BaseGrapheneHandler {

    // Sequence of message ids
    private final static int LOGIN_ID = 1;
    private final static int GET_DATABASE_ID = 2;
    private final static int GET_BLOCK_HEADER_ID = 3;

    private long blockNumber;
    private WitnessResponseListener mListener;

    private int currentId = LOGIN_ID;
    private int apiId = -1;

    private boolean mOneTime;

    /**
     * Default Constructor
     *
     * @param blockNumber   height of the block whose header should be returned
     * @param oneTime       boolean value indicating if WebSocket must be closed (true) or not
     *                      (false) after the response
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetBlockHeader(long blockNumber, boolean oneTime, WitnessResponseListener listener){
        super(listener);
        this.blockNumber = blockNumber;
        this.mOneTime = oneTime;
        this.mListener = listener;
    }

    /**
     * Using this constructor the WebSocket connection closes after the response.
     *
     * @param blockNumber   height of the block whose header should be returned
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetBlockHeader(long blockNumber, WitnessResponseListener listener){
        this(blockNumber, true, listener);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
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
        }else {
            currentId++;
            ArrayList<Serializable> emptyParams = new ArrayList<>();
            if(baseResponse.id == LOGIN_ID){
                ApiCall getDatabaseId = new ApiCall(1, RPC.CALL_DATABASE, emptyParams, RPC.VERSION, currentId);
                websocket.sendText(getDatabaseId.toJsonString());
            }else if(baseResponse.id == GET_DATABASE_ID){
                Type ApiIdResponse = new TypeToken<WitnessResponse<Integer>>() {}.getType();
                WitnessResponse<Integer> witnessResponse = gson.fromJson(response, ApiIdResponse);
                apiId = witnessResponse.result.intValue();

                ArrayList<Serializable> params = new ArrayList<>();
                String blockNum = String.format("%d", this.blockNumber);
                params.add(blockNum);

                ApiCall loginCall = new ApiCall(apiId, RPC.CALL_GET_BLOCK_HEADER, params, RPC.VERSION, currentId);
                websocket.sendText(loginCall.toJsonString());
            }else if(baseResponse.id == GET_BLOCK_HEADER_ID){
                Type RelativeAccountHistoryResponse = new TypeToken<WitnessResponse<BlockHeader>>(){}.getType();
                WitnessResponse<BlockHeader> transfersResponse = gson.fromJson(response, RelativeAccountHistoryResponse);
                mListener.onSuccess(transfersResponse);
                if(mOneTime){
                    websocket.disconnect();
                }
            }
        }

    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        if(frame.isTextFrame())
            System.out.println(">>> "+frame.getPayloadText());
    }
}
