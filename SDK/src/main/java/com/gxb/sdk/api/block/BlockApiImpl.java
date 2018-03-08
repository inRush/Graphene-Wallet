package com.gxb.sdk.api.block;

import com.gxb.sdk.api.base.BaseApi;
import com.gxb.sdk.api.base.paramconverters.EmptyParamConverter;
import com.gxb.sdk.api.base.paramconverters.SingleParamConverter;
import com.gxb.sdk.http.GxbCallBack;

import org.json.JSONObject;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public class BlockApiImpl extends BaseApi implements BlockApi {
    @Override
    public void getBlock(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_block", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getBlockHeader(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_block_header", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getChainId(String paramStr, GxbCallBack callBack) {
        JSONObject object = EmptyParamConverter.getInstance()
                .convertParam(0, "get_chain_id", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getChainProperties(String paramStr, GxbCallBack callBack) {
        JSONObject object = EmptyParamConverter.getInstance()
                .convertParam(0, "get_chain_properties", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getCommissionPercent(String paramStr, GxbCallBack callBack) {
        JSONObject object = EmptyParamConverter.getInstance()
                .convertParam(0, "get_commission_percent", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getConfig(String paramStr, GxbCallBack callBack) {
        JSONObject object = EmptyParamConverter.getInstance()
                .convertParam(0, "get_config", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getDynamicGlobalProperties(String paramStr, GxbCallBack callBack) {
        JSONObject object = EmptyParamConverter.getInstance()
                .convertParam(0, "get_dynamic_global_properties", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getGlobalProperties(String paramStr, GxbCallBack callBack) {
        JSONObject object = EmptyParamConverter.getInstance()
                .convertParam(0, "get_global_properties", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getRecentTransactionById(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_recent_transaction_by_id", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getTransaction(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_transaction", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }
}
