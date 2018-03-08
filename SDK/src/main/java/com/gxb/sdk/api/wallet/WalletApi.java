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
     * @param paramStr <username>
     * @param callBack 结果回调
     */
    void getAccountByName(String paramStr, GxbCallBack callBack);

    /**
     * 获取资产账户信息
     *
     * @param paramStr <assets>,<id>
     * @param callBack
     */
    void getAccountBalances(String paramStr, GxbCallBack callBack);

    /**
     * 获取链上注册的所有账户数量
     *
     * @param paramStr Empty String
     * @param callBack
     */
    void getAccountCount(String paramStr, GxbCallBack callBack);

    /**
     * 获取账户account_id相关的账户id
     *
     * @param paramStr <account_id>
     * @param callBack
     */
    void getAccountReferences(String paramStr, GxbCallBack callBack);

    /**
     * 账户查询
     *
     * @param paramStr <account_ids> example : "1.2.1,1.2.3"
     * @param callBack
     */
    void getAccounts(String paramStr, GxbCallBack callBack);

    /**
     * 通过资产ID获取资产
     *
     * @param paramStr <asset_ids> example : "1.2.1,1.2.3"
     * @param callBack
     */
    void getAssets(String paramStr, GxbCallBack callBack);

    /**
     * 返回地址address上所有未领取的余额对象
     * @param paramStr <<[address]>>
     * @param callBack
     */
    void getBalanceObjects(String paramStr, GxbCallBack callBack);

    /**
     * 获取符合条件的所有账户相关信息
     * @param paramStr <names_or_ids> <subscribe>
     * @param callBack
     */
    void getFullAccounts(String paramStr, GxbCallBack callBack);

    /**
     * 返回所有指向公钥的帐户信息
     * @param paramStr <key>
     * @param callBack
     */
    void getKeyReferences(String paramStr, GxbCallBack callBack);

    /**
     * 通过账户名和资产ID获取账户资产信息
     * @param paramStr
     * @param callBack
     */
    void getNamedAccountBalances(String paramStr, GxbCallBack callBack);

    /**
     * 通过账户余额ID获取可领取的资产信息
     * @param paramStr <objs>
     * @param callBack
     */
    void getVestedBalances(String paramStr, GxbCallBack callBack);

    /**
     * 通过账户ID获取归属该账户但暂时不可领取的余额信息
     * @param paramStr <account_id>
     * @param callBack
     */
    void getVestingBalances(String paramStr, GxbCallBack callBack);

    /**
     * 验证公钥是否已经被注册
     * @param paramStr <public_key>
     * @param callBack
     */
    void isPublicKeyRegistered(String paramStr, GxbCallBack callBack);

    /**
     * 通过资产符号名称获取资产信息，并返回前limit个
     * @param paramStr <lower_bound_symbol> <limit>
     * @param callBack
     */
    void listAssets(String paramStr, GxbCallBack callBack);

    /**
     * 通过账户名获取账户信息
     * @param paramStr <account_names>
     * @param callBack
     */
    void lookupAccountNames(String paramStr, GxbCallBack callBack);

    /**
     * 获取已注册账户的账户名和ID
     * @param paramStr <limit> <lower_bound_name>
     * @param callBack
     */
    void lookupAccounts(String paramStr, GxbCallBack callBack);

    /**
     * 通过资产符号获取资产列表
     * @param paramStr <symbols_or_ids>
     * @param callBack
     */
    void lookupAssetSymbols(String paramStr, GxbCallBack callBack);
}
