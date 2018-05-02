package cy.agorise.graphenej.api;

import java.io.Serializable;
import java.util.ArrayList;

import cy.agorise.graphenej.RPC;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.ApiCall;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 * @author inrush
 * @date 2018/4/4.
 */

public abstract class BaseRpcHandler {


    protected WebSocketService mService;
    /**
     * 通过WebSocketService的apiCode获取对应类型的ID
     *
     * @return API ID
     */
    protected abstract int getApiId();

    /**
     * 获取方法名称
     *
     * @return API Name
     */
    protected abstract String getApiName();

    /**
     * 获取方法的参数
     *
     * @return Method Params
     */
    protected abstract ArrayList<Serializable> getParams();

    /**
     * 转换响应
     *
     * @param result 响应的字符串
     * @return 响应
     */
    protected abstract WitnessResponse onResponse(String result);
    public WebSocketService getService() {
        return mService;
    }
    public BaseRpcHandler(WebSocketService service){
        mService = service;
    }
    public void call(final WitnessResponseListener listener) {
        try {
            ApiCall call = new ApiCall(getApiId(),
                    getApiName(), getParams(), RPC.VERSION, mService.generateId());
            mService.call(call, new WebSocketService.OnResponseListener() {
                        @Override
                        public void onSuccess(String result) {
                            WitnessResponse witnessResponse = onResponse(result);
                            if (witnessResponse.error != null) {
                                listener.onError(witnessResponse.error);
                            } else {
                                listener.onSuccess(witnessResponse);
                            }
                        }

                        @Override
                        public void onError(BaseResponse.Error error) {
                            listener.onError(error);
                        }
                    });


        } catch (Exception e) {
            listener.onError(new BaseResponse.Error(e.getMessage()));
        } catch (Throwable t) {
            listener.onError(new BaseResponse.Error(t.getMessage()));
        }
    }
}
