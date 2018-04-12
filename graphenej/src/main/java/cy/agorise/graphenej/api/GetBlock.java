package cy.agorise.graphenej.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.models.Block;
import cy.agorise.graphenej.models.WitnessResponse;
import cy.agorise.graphenej.objects.Memo;
import cy.agorise.graphenej.operations.CustomOperation;
import cy.agorise.graphenej.operations.LimitOrderCreateOperation;
import cy.agorise.graphenej.operations.TransferOperation;

/**
 * @author inrush
 */
public class GetBlock extends BaseRpcHandler {

    private long blockNumber;

    public GetBlock(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    @Override
    protected int getApiId() {
        return WebSocketService.getInstance().apiCode.getDataseId();
    }

    @Override
    protected String getApiName() {
        return RPC.CALL_GET_BLOCK;
    }

    @Override
    protected ArrayList<Serializable> getParams() {
        ArrayList<Serializable> params = new ArrayList<>();
        String blockNum = String.format(Locale.CHINA, "%d", this.blockNumber);
        params.add(blockNum);
        return params;
    }

    @Override
    protected WitnessResponse onResponse(String result) {
        Type BlockResponse = new TypeToken<WitnessResponse<Block>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new Transaction.TransactionDeserializer())
                .registerTypeAdapter(TransferOperation.class, new TransferOperation.TransferDeserializer())
                .registerTypeAdapter(LimitOrderCreateOperation.class, new LimitOrderCreateOperation.LimitOrderCreateDeserializer())
                .registerTypeAdapter(CustomOperation.class, new CustomOperation.CustomOperationDeserializer())
                .registerTypeAdapter(AssetAmount.class, new AssetAmount.AssetAmountDeserializer())
                .registerTypeAdapter(Memo.class, new Memo.MemoDeserializer())
                .create();
        return gson.fromJson(result, BlockResponse);
    }
}
