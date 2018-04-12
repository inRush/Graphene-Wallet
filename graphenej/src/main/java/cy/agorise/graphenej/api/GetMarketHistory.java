package cy.agorise.graphenej.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.ApiCall;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.BucketObject;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 *  Class that implements get_market_history request handler.
 *
 *  Get mar
 *
 *  @see <a href="https://goo.gl/hfVFBW">get_market_history API doc</a>
 *
 */
public class GetMarketHistory extends BaseGrapheneHandler {

    // Sequence of message ids
    private final static int LOGIN_ID = 1;
    private final static int GET_HISTORY_ID = 2;
    private final static int GET_HISTORY_DATA = 3;

    // API call parameters
    private Asset base;
    private Asset quote;
    private long bucket;
    private Date start;
    private Date end;
    private WitnessResponseListener mListener;
    private WebSocket mWebsocket;
    private int currentId = 1;
    private int apiId = -1;
    private int counter = 0;

    private boolean mOneTime;

    /**
     * Default Constructor
     *
     * @param base          asset which history is desired
     * @param quote         asset which the base price asset will be compared to
     * @param bucket        the time interval (in seconds) for each point should be (analog to
     *                      candles on a candle stick graph).
     *                      Note: The bucket value is discrete and node dependent. The default value
     *                      is 3600s. To get the available buckets of a node use
     *                      get_all_asset_holders API call.
     * @param start         datetime of of the most recent operation to retrieve (Note: The name is
     *                      counter intuitive, but it follow the original API parameter name)
     * @param end           datetime of the the earliest operation to retrieve
     * @param oneTime       boolean value indicating if WebSocket must be closed (true) or not
     *                      (false) after the response
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetMarketHistory(Asset base, Asset quote, long bucket, Date start, Date end, boolean oneTime, WitnessResponseListener listener){
        super(listener);
        this.base = base;
        this.quote = quote;
        this.bucket = bucket;
        this.start = start;
        this.end = end;
        this.mOneTime = oneTime;
        this.mListener = listener;
    }

    /**
     * Using this constructor the WebSocket connection closes after the response.
     *
     * @param base          asset which history is desired
     * @param quote         asset which the base price asset will be compared to
     * @param bucket        the time interval (in seconds) for each point should be (analog to
     *                      candles on a candle stick graph).
     *                      Note: The bucket value is discrete and node dependent. The default value
     *                      is 3600s. To get the available buckets of a node use
     *                      get_all_asset_holders API call.
     * @param start         datetime of of the most recent operation to retrieve (Note: The name is
     *                      counter intuitive, but it follow the original API parameter name)
     * @param end           datetime of the the earliest operation to retrieve
     * @param listener      A class implementing the WitnessResponseListener interface. This should
     *                      be implemented by the party interested in being notified about the
     *                      success/failure of the operation.
     */
    public GetMarketHistory(Asset base, Asset quote, long bucket, Date start, Date end, WitnessResponseListener listener){
        this(base, quote, bucket, start, end, true, listener);
    }

    public Asset getBase() {
        return base;
    }

    public void setBase(Asset base) {
        this.base = base;
    }

    public Asset getQuote() {
        return quote;
    }

    public void setQuote(Asset quote) {
        this.quote = quote;
    }

    public long getBucket() {
        return bucket;
    }

    public void setBucket(long bucket) {
        this.bucket = bucket;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getCount(){
        return this.counter;
    }

    public void disconnect(){
        if(mWebsocket != null && mWebsocket.isOpen()){
            mWebsocket.disconnect();
        }
    }

    /**
     * Retries the 'get_market_history' API call.
     * Hopefully with different 'start' and 'stop' parameters.
     */
    public void retry(){
        sendHistoricalMarketDataRequest();
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
            } else if(baseResponse.id == GET_HISTORY_ID){
                Type ApiIdResponse = new TypeToken<WitnessResponse<Integer>>() {}.getType();
                WitnessResponse<Integer> witnessResponse = gson.fromJson(response, ApiIdResponse);
                apiId = witnessResponse.result.intValue();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

                ArrayList<Serializable> params = new ArrayList<>();
                params.add(this.base.getObjectId());
                params.add(this.quote.getObjectId());
                params.add(this.bucket);
                params.add(dateFormat.format(this.start));
                params.add(dateFormat.format(this.end));

                ApiCall getRelativeAccountHistoryCall = new ApiCall(apiId, RPC.CALL_GET_MARKET_HISTORY, params, RPC.VERSION, currentId);
                websocket.sendText(getRelativeAccountHistoryCall.toJsonString());
            }else if(baseResponse.id >= GET_HISTORY_DATA){
                GsonBuilder builder = new GsonBuilder();
                Type MarketHistoryResponse = new TypeToken<WitnessResponse<List<BucketObject>>>(){}.getType();
                builder.registerTypeAdapter(BucketObject.class, new BucketObject.BucketDeserializer());
                WitnessResponse<List<BucketObject>> marketHistoryResponse = builder.create().fromJson(response, MarketHistoryResponse);
                mListener.onSuccess(marketHistoryResponse);
            }
        }
    }

    /**
     * Actually sends the 'get_market_history' API call request. This method might be called multiple
     * times during the life-cycle of this instance because we might not have gotten anything
     * in the first requested interval.
     */
    private void sendHistoricalMarketDataRequest(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

        ArrayList<Serializable> params = new ArrayList<>();
        params.add(this.base.getObjectId());
        params.add(this.quote.getObjectId());
        params.add(this.bucket);
        params.add(dateFormat.format(this.start));
        params.add(dateFormat.format(this.end));

        ApiCall getRelativeAccountHistoryCall = new ApiCall(apiId, RPC.CALL_GET_MARKET_HISTORY, params, RPC.VERSION, currentId);
        mWebsocket.sendText(getRelativeAccountHistoryCall.toJsonString());

        counter++;
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        if(frame.isTextFrame())
            System.out.println(">>> "+frame.getPayloadText());
    }
}
