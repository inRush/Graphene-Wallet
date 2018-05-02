package cy.agorise.graphenej.api;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 * @author inrush
 * @date 2018/4/30.
 */

public class GetRequiredFees extends BaseRpcHandler {
    private String mAssetId;
    private List<BaseOperation> mOperations;

    public GetRequiredFees(WebSocketService service, String assetId, List<BaseOperation> operations) {
        super(service);
        this.mAssetId = assetId;
        this.mOperations = operations;
    }

    @Override
    protected int getApiId() {
        return mService.apiCode.getDataseId();
    }

    @Override
    protected String getApiName() {
        return RPC.CALL_GET_REQUIRED_FEES;
    }

    @Override
    protected ArrayList<Serializable> getParams() {
        ArrayList<Serializable> accountParams = new ArrayList<>();
        accountParams.add((Serializable) mOperations);
        accountParams.add(mAssetId);
        return accountParams;
    }

    @Override
    protected WitnessResponse onResponse(String result) {
        Type GetRequiredFeesResponse = new TypeToken<WitnessResponse<List<AssetAmount>>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AssetAmount.class, new AssetAmount.AssetAmountDeserializer());
        return gsonBuilder.create().fromJson(result, GetRequiredFeesResponse);
    }
}
