package cy.agorise.graphenej.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cy.agorise.graphenej.AccountOptions;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.Authority;
import cy.agorise.graphenej.GrapheneObject;
import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.models.BitAssetData;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 * Class that implements get_objects request handler.
 * <p>
 * Get the objects corresponding to the provided IDs.
 * <p>
 * The response returns a list of objects retrieved, in the order they are mentioned in ids
 *
 * @author inrush
 * @see <a href="https://goo.gl/isRfeg">get_objects API doc</a>
 */
public class GetObjects extends BaseRpcHandler {
    private List<String> ids;

    /**
     * Default Constructor
     *
     * @param ids      list of IDs of the objects to retrieve
     */
    public GetObjects(List<String> ids) {
        this.ids = ids;
    }

//    /**
//     * Using this constructor the WebSocket connection closes after the response.
//     *
//     * @param ids      list of IDs of the objects to retrieve
//     * @param listener A class implementing the WitnessResponseListener interface. This should
//     *                 be implemented by the party interested in being notified about the
//     *                 success/failure of the operation.
//     */
//    public GetObjects(List<String> ids, WitnessResponseListener listener) {
//        this(ids, true, listener);
//    }

//    @Override
//    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
//        ArrayList<Serializable> params = new ArrayList<>();
//        ArrayList<Serializable> subParams = new ArrayList<>();
//        for (String id : this.ids) {
//            subParams.add(id);
//        }
//        params.add(subParams);
//        ApiCall apiCall = new ApiCall(0, RPC.GET_OBJECTS, params, RPC.VERSION, 0);
//        websocket.sendText(apiCall.toJsonString());
//    }
//
//    @Override
//    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
//        if (frame.isTextFrame()) {
//            System.out.println("<< " + frame.getPayloadText());
//        }
//        String response = frame.getPayloadText();
//        GsonBuilder gsonBuilder = new GsonBuilder();
//
//        gsonBuilder.registerTypeAdapter(AssetAmount.class, new AssetAmount.AssetAmountDeserializer());
//        gsonBuilder.registerTypeAdapter(Asset.class, new Asset.AssetDeserializer());
//        gsonBuilder.registerTypeAdapter(UserAccount.class, new UserAccount.UserAccountFullDeserializer());
//        gsonBuilder.registerTypeAdapter(Authority.class, new Authority.AuthorityDeserializer());
//        gsonBuilder.registerTypeAdapter(AccountOptions.class, new AccountOptions.AccountOptionsDeserializer());
//        Gson gson = gsonBuilder.create();
//
//        List<GrapheneObject> parsedResult = new ArrayList<>();
//
//        JsonParser parser = new JsonParser();
//        JsonArray resultArray = parser.parse(response).getAsJsonObject().get(WitnessResponse.KEY_RESULT).getAsJsonArray();
//        for (JsonElement element : resultArray) {
//            String id = element.getAsJsonObject().get(GrapheneObject.KEY_ID).getAsString();
//            GrapheneObject grapheneObject = new GrapheneObject(id);
//            switch (grapheneObject.getObjectType()) {
//                case ASSET_OBJECT:
//                    Asset asset = gson.fromJson(element, Asset.class);
//                    parsedResult.add(asset);
//                    break;
//                case ACCOUNT_OBJECT:
//                    UserAccount account = gson.fromJson(element, UserAccount.class);
//                    parsedResult.add(account);
//                    break;
//                case ASSET_BITASSET_DATA:
//                    Type BitAssetDataType = new TypeToken<WitnessResponse<List<BitAssetData>>>() {
//                    }.getType();
//                    WitnessResponse<List<BitAssetData>> witnessResponse = gsonBuilder.create().fromJson(response, BitAssetDataType);
//                    BitAssetData bitAssetData = witnessResponse.result.get(0);
//                    parsedResult.add(bitAssetData);
//            }
//        }
//
//        WitnessResponse<List<GrapheneObject>> output = new WitnessResponse<>();
//        output.result = parsedResult;
//        mListener.onSuccess(output);
//        if (mOneTime) {
//            websocket.disconnect();
//        }
//    }
//
//    @Override
//    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
//        if (frame.isTextFrame()) {
//            System.out.println(">> " + frame.getPayloadText());
//        }
//    }

    @Override
    protected int getApiId() {
        return WebSocketService.getInstance().apiCode.getDataseId();
    }

    @Override
    protected String getApiName() {
        return RPC.GET_OBJECTS;
    }

    @Override
    protected ArrayList<Serializable> getParams() {
        ArrayList<Serializable> params = new ArrayList<>();
        ArrayList<Serializable> subParams = new ArrayList<>();
        subParams.addAll(this.ids);
        params.add(subParams);
        return params;
    }

    @Override
    protected WitnessResponse onResponse(String result) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(AssetAmount.class, new AssetAmount.AssetAmountDeserializer());
        gsonBuilder.registerTypeAdapter(Asset.class, new Asset.AssetDeserializer());
        gsonBuilder.registerTypeAdapter(UserAccount.class, new UserAccount.UserAccountFullDeserializer());
        gsonBuilder.registerTypeAdapter(Authority.class, new Authority.AuthorityDeserializer());
        gsonBuilder.registerTypeAdapter(AccountOptions.class, new AccountOptions.AccountOptionsDeserializer());
        Gson gson = gsonBuilder.create();

        List<GrapheneObject> parsedResult = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonArray resultArray = parser.parse(result).getAsJsonObject().get(WitnessResponse.KEY_RESULT).getAsJsonArray();
        for (JsonElement element : resultArray) {
            String id = element.getAsJsonObject().get(GrapheneObject.KEY_ID).getAsString();
            GrapheneObject grapheneObject = new GrapheneObject(id);
            switch (grapheneObject.getObjectType()) {
                case ASSET_OBJECT:
                    Asset asset = gson.fromJson(element, Asset.class);
                    parsedResult.add(asset);
                    break;
                case ACCOUNT_OBJECT:
                    UserAccount account = gson.fromJson(element, UserAccount.class);
                    parsedResult.add(account);
                    break;
                case ASSET_BITASSET_DATA:
                    Type BitAssetDataType = new TypeToken<WitnessResponse<List<BitAssetData>>>() {
                    }.getType();
                    WitnessResponse<List<BitAssetData>> witnessResponse = gsonBuilder.create().fromJson(result, BitAssetDataType);
                    BitAssetData bitAssetData = witnessResponse.result.get(0);
                    parsedResult.add(bitAssetData);
                default:
                    break;
            }
        }

        WitnessResponse<List<GrapheneObject>> output = new WitnessResponse<>();
        output.result = parsedResult;
        return output;
    }
}
