package com.gxb.sdk.api.wallet;

import com.gxb.sdk.Config;
import com.gxb.sdk.api.base.BaseApi;
import com.gxb.sdk.http.GxbCallBack;
import com.gxb.sdk.models.RpcRequest;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public class WalletApiImpl extends BaseApi implements WalletApi {

    public WalletApiImpl() {
    }

    @Override
    public void getAccountByName(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "get_account_by_name", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getAccountBalances(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "get_account_balances", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getAccountCount(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "get_account_count", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getAccountReferences(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_account_references", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getAccounts(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "get_accounts", params, true);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getAssets(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_assets", params, true);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getBalanceObjects(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_balance_objects", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getFullAccounts(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_full_accounts", params, true);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getKeyReferences(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_key_references", params, true);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getNamedAccountBalances(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_named_account_balances", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getVestedBalances(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_vested_balances", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void getVestingBalances(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "get_vesting_balances", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void isPublicKeyRegistered(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "is_public_key_registered", params, true);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void listAssets(Object[] params, GxbCallBack callBack) {

        String json = RpcRequest.createRequest(0, "list_assets", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void lookupAccountNames(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "lookup_account_names", params, true);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void lookupAccounts(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "lookup_accounts", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void lookupAssetSymbols(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "lookup_asset_symbols", params, false);
        mHttpRequest.doCallRpc(json, callBack);
    }

    @Override
    public void transfer(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "transfer", params, false);
//        mHttpRequest.doCallRpc(json, Config.RPC_URL_DEV_LOCAL, callBack);
    }

    @Override
    public void transfer2(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest(0, "transfer2", params, false);
//        mHttpRequest.doCallRpc(json, Config.RPC_URL_DEV_LOCAL, callBack);
    }

    @Override
    public void registerAccount2(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest("register_account2", params);
        mHttpRequest.doCallRpc(json, Config.RPC_URL_DEV_LOCAL_GXB, callBack);
    }

    @Override
    public void registerAccount(Object[] params, GxbCallBack callBack) {
        String json = RpcRequest.createRequest("register_account", params);
        mHttpRequest.doCallRpc(json, Config.RPC_URL_DEV_LOCAL_BTS, callBack);
    }
}
