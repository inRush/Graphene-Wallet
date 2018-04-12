package com.gxb.sdk.api.register.global;

import com.gxb.sdk.api.base.BaseApi;
import com.gxb.sdk.http.GxbCallBack;
import com.gxb.sdk.models.RpcRequest;

/**
 * @author inrush
 * @date 2018/3/12.
 */

public class GlobalApiImpl extends BaseApi implements GlobalApi {
    @Override
    public void getObjects(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "get_objects", params, true);
        mHttpRequest.doCallRpc(json, callBack);
    }
}
