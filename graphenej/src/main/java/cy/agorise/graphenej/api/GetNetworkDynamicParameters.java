package cy.agorise.graphenej.api;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.models.DynamicGlobalProperties;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 * @author inrush
 * @date 2018/4/30.
 */

public class GetNetworkDynamicParameters extends BaseRpcHandler {
    public GetNetworkDynamicParameters(WebSocketService service) {
        super(service);
    }

    @Override
    protected int getApiId() {
        return mService.apiCode.getDatabaseId();
    }

    @Override
    protected String getApiName() {
        return RPC.CALL_GET_DYNAMIC_GLOBAL_PROPERTIES;
    }

    @Override
    protected ArrayList<Serializable> getParams() {
        return new ArrayList<>();
    }

    @Override
    protected WitnessResponse onResponse(String result) {
        Type DynamicGlobalPropertiesResponse = new TypeToken<WitnessResponse<DynamicGlobalProperties>>(){}.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DynamicGlobalProperties.class, new DynamicGlobalProperties.DynamicGlobalPropertiesDeserializer());
        return builder.create().fromJson(result, DynamicGlobalPropertiesResponse);
    }
}
