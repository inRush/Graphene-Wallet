package com.gxb.sdk.api.wallet;

import com.gxb.sdk.api.base.BaseApi;
import com.gxb.sdk.api.base.paramconverters.EmptyParamConverter;
import com.gxb.sdk.api.base.paramconverters.MultipleParamConverter;
import com.gxb.sdk.api.base.paramconverters.SingleParamConverter;
import com.gxb.sdk.http.GxbCallBack;

import org.json.JSONObject;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public class WalletApiImpl extends BaseApi implements WalletApi {

    public WalletApiImpl() {
    }

    @Override
    public void getAccountByName(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_account_by_name", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getAccountBalances(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_account_balances", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getAccountCount(String paramStr, GxbCallBack callBack) {
        JSONObject object = EmptyParamConverter.getInstance()
                .convertParam(0, "get_account_count", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getAccountReferences(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_account_references", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getAccounts(String paramStr, GxbCallBack callBack) {
        JSONObject object = MultipleParamConverter.getInstance()
                .convertParam(0, "get_accounts", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getAssets(String paramStr, GxbCallBack callBack) {
        JSONObject object = MultipleParamConverter.getInstance()
                .convertParam(0, "get_assets", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getBalanceObjects(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_balance_objects", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getFullAccounts(String paramStr, GxbCallBack callBack) {
        JSONObject object = MultipleParamConverter.getInstance()
                .convertParam(0, "get_full_accounts", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getKeyReferences(String paramStr, GxbCallBack callBack) {
        JSONObject object = MultipleParamConverter.getInstance()
                .convertParam(0, "get_key_references", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getNamedAccountBalances(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_named_account_balances", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getVestedBalances(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_vested_balances", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void getVestingBalances(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "get_vesting_balances", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void isPublicKeyRegistered(String paramStr, GxbCallBack callBack) {
        JSONObject object = MultipleParamConverter.getInstance()
                .convertParam(0, "is_public_key_registered", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void listAssets(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "list_assets", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void lookupAccountNames(String paramStr, GxbCallBack callBack) {
        JSONObject object = MultipleParamConverter.getInstance()
                .convertParam(0, "lookup_account_names", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void lookupAccounts(String paramStr, GxbCallBack callBack) {
        JSONObject object = SingleParamConverter.getInstance()
                .convertParam(0, "lookup_accounts", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }

    @Override
    public void lookupAssetSymbols(String paramStr, GxbCallBack callBack) {
        JSONObject object = MultipleParamConverter.getInstance()
                .convertParam(0, "lookup_asset_symbols", paramStr);
        mHttpRequest.doCallRpc(object, callBack);
    }
}
