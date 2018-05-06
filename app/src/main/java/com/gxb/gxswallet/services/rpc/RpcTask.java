package com.gxb.gxswallet.services.rpc;

import com.gxb.gxswallet.common.Task;

import cy.agorise.graphenej.api.BaseRpcHandler;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.WitnessResponse;
import io.reactivex.Observable;

/**
 * @author inrush
 * @date 2018/4/26.
 */

public class RpcTask extends Task<WitnessResponse, RpcTask> {
    /**
     * 请求
     */
    private BaseRpcHandler mRpc;

    public RpcTask(BaseRpcHandler handler, String tag) {
        super(tag);
        this.mRpc = handler;
    }

    public BaseRpcHandler getRpc() {
        return mRpc;
    }

    @Override
    protected Observable<RpcTask> runTask() {
        if (mRpc == null) {
            return Observable.empty();
        }
        if (getRpc().getService() == null || !getRpc().getService().isInitializedComplete()) {
            return Observable.error(new Throwable("数据重连中,请稍后再试"));
        }
        return Observable.create(e -> mRpc.call(new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                taskFinish(response);
                e.onNext(RpcTask.this);
                e.onComplete();
            }

            @Override
            public void onError(BaseResponse.Error error) {
                taskError();
                e.onError(new Throwable(error.message));
            }
        }));
    }
}
