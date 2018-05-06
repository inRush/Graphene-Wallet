package cy.agorise.graphenej.api;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cy.agorise.graphenej.AccountOptions;
import cy.agorise.graphenej.Authority;
import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 * Class that implements get_account_by_name request handler.
 * <p>
 * Get an account’s info by name.
 * <p>
 * The response returns account data that refer to the name.
 *
 * @author inrush
 * @see <a href="https://goo.gl/w75qjV">get_account_by_name API doc</a>
 */
public class GetAccountByName extends BaseRpcHandler {

    private String accountName;

    public GetAccountByName(WebSocketService service, String accountName) {
        super(service);
        this.accountName = accountName;
    }

    @Override
    protected int getApiId() {
        return mService.apiCode.getDatabaseId();
    }

    @Override
    protected String getApiName() {
        return RPC.CALL_GET_ACCOUNT_BY_NAME;
    }

    @Override
    protected ArrayList<Serializable> getParams() {
        ArrayList<Serializable> params = new ArrayList<>();
        params.add(this.accountName);
        return params;
    }

    @Override
    protected WitnessResponse onResponse(String result) {
        GsonBuilder builder = new GsonBuilder();
        Type getAccountByNameResponse = new TypeToken<WitnessResponse<AccountProperties>>() {
        }.getType();
        builder.registerTypeAdapter(Authority.class, new Authority.AuthorityDeserializer());
        builder.registerTypeAdapter(AccountOptions.class, new AccountOptions.AccountOptionsDeserializer());
        return builder.create().fromJson(result, getAccountByNameResponse);
    }
}
