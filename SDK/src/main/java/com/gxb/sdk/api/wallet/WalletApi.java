package com.gxb.sdk.api.wallet;

import com.gxb.sdk.http.GxbCallBack;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public interface WalletApi {
    /**
     * 通过账户名获取账户信息
     *
     * @param params   <username>
     * @param callBack 结果回调
     */
    void getAccountByName(Object[] params, GxbCallBack callBack);

    /**
     * 获取资产账户信息
     *
     * @param params   <assets>,<id>
     * @param callBack
     */
    void getAccountBalances(Object[] params, GxbCallBack callBack);

    /**
     * 获取链上注册的所有账户数量
     *
     * @param params   Empty String
     * @param callBack
     */
    void getAccountCount(Object[] params, GxbCallBack callBack);

    /**
     * 获取账户account_id相关的账户id
     *
     * @param params   <account_id>
     * @param callBack
     */
    void getAccountReferences(Object[] params, GxbCallBack callBack);

    /**
     * 账户查询
     *
     * @param params   <account_ids> example : "1.2.1,1.2.3"
     * @param callBack
     */
    void getAccounts(Object[] params, GxbCallBack callBack);

    /**
     * 通过资产ID获取资产
     *
     * @param params   <asset_ids> example : "1.2.1,1.2.3"
     * @param callBack
     */
    void getAssets(Object[] params, GxbCallBack callBack);

    /**
     * 返回地址address上所有未领取的余额对象
     *
     * @param params   <<[address]>>
     * @param callBack
     */
    void getBalanceObjects(Object[] params, GxbCallBack callBack);

    /**
     * 获取符合条件的所有账户相关信息
     *
     * @param params   <names_or_ids> <subscribe>
     * @param callBack
     */
    void getFullAccounts(Object[] params, GxbCallBack callBack);

    /**
     * 返回所有指向公钥的帐户信息
     *
     * @param params   <key>
     * @param callBack
     */
    void getKeyReferences(Object[] params, GxbCallBack callBack);

    /**
     * 通过账户名和资产ID获取账户资产信息
     *
     * @param params
     * @param callBack
     */
    void getNamedAccountBalances(Object[] params, GxbCallBack callBack);

    /**
     * 通过账户余额ID获取可领取的资产信息
     *
     * @param params   <objs>
     * @param callBack
     */
    void getVestedBalances(Object[] params, GxbCallBack callBack);

    /**
     * 通过账户ID获取归属该账户但暂时不可领取的余额信息
     *
     * @param params   <account_id>
     * @param callBack
     */
    void getVestingBalances(Object[] params, GxbCallBack callBack);

    /**
     * 验证公钥是否已经被注册
     *
     * @param params   <public_key>
     * @param callBack
     */
    void isPublicKeyRegistered(Object[] params, GxbCallBack callBack);

    /**
     * 通过资产符号名称获取资产信息，并返回前limit个
     *
     * @param params   <lower_bound_symbol> <limit>
     * @param callBack
     */
    void listAssets(Object[] params, GxbCallBack callBack);

    /**
     * 通过账户名获取账户信息
     *
     * @param params   <account_names>
     * @param callBack
     */
    void lookupAccountNames(Object[] params, GxbCallBack callBack);

    /**
     * 获取已注册账户的账户名和ID
     *
     * @param params   <limit> <lower_bound_name>
     * @param callBack
     */
    void lookupAccounts(Object[] params, GxbCallBack callBack);

    /**
     * 通过资产符号获取资产列表
     *
     * @param params   <symbols_or_ids>
     * @param callBack
     */
    void lookupAssetSymbols(Object[] params, GxbCallBack callBack);

    /**
     * 转账 不返回交易ID
     *
     * @param params   gxb1 gxb2 100 GXS "transfer memo" true
     * @param callBack
     */
    void transfer(Object[] params, GxbCallBack callBack);

    /**
     * 转账 返回交易ID
     *
     * @param params   gxb1 gxb2 100 GXS "transfer memo" true
     * @param callBack
     */
    void transfer2(Object[] params, GxbCallBack callBack);


    void registerAccount2(Object[] params, GxbCallBack callBack);
    void registerAccount(Object[] params, GxbCallBack callBack);
}
