package cy.agorise.graphenej.api;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.models.HistoricalTransfer;
import cy.agorise.graphenej.models.WitnessResponse;
import cy.agorise.graphenej.objects.Memo;
import cy.agorise.graphenej.operations.TransferOperation;

/**
 * @author inrush
 * @date 04/04/18
 */
public class GetAccountHistory extends BaseRpcHandler {

    private String mAccountId;
    private int mLimit;

    private static final String START = "1.11.0";
    private static final String STOP = "1.11.0";


    public GetAccountHistory(String accountId, int limit) {
        mAccountId = accountId;
        mLimit = limit;

    }

    @Override
    protected int getApiId() {
        return WebSocketService.getInstance().apiCode.getHistoryId();
    }

    @Override
    protected String getApiName() {
        return RPC.CALL_GET_ACCOUNT_HISTORY;
    }

    @Override
    protected ArrayList<Serializable> getParams() {
        ArrayList<Serializable> params = new ArrayList<>();
        params.add(mAccountId);
        params.add(START);
        params.add(mLimit);
        params.add(STOP);
        return params;
    }

    @Override
    protected WitnessResponse onResponse(String result) {
        Type getAccountHistoryResponse = new TypeToken<WitnessResponse<List<HistoricalTransfer>>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TransferOperation.class, new TransferOperation.TransferDeserializer());
        gsonBuilder.registerTypeAdapter(AssetAmount.class, new AssetAmount.AssetAmountDeserializer());
        gsonBuilder.registerTypeAdapter(Memo.class, new Memo.MemoDeserializer());
        return gsonBuilder.create().fromJson(result, getAccountHistoryResponse);
    }
}
