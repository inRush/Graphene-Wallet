package com.gxb.sdk.api.block;

import com.gxb.sdk.http.GxbCallBack;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public interface BlockApi {
    /**
     * 获取区块信息
     * @param paramStr <block_num>
     * @param callBack
     */
    void getBlock(String paramStr, GxbCallBack callBack);

    /**
     * 获取区块头信息
     * @param paramStr <block_num>
     * @param callBack
     */
    void getBlockHeader(String paramStr, GxbCallBack callBack);

    /**
     * 获取链ID
     * @param paramStr Empty String
     * @param callBack
     */
    void getChainId(String paramStr, GxbCallBack callBack);

    /**
     * 获取链属性
     * @param paramStr Empty String
     * @param callBack
     */
    void getChainProperties(String paramStr, GxbCallBack callBack);

    /**
     * 获取佣金比例
     * @param paramStr Empty String
     * @param callBack
     */
    void getCommissionPercent(String paramStr, GxbCallBack callBack);

    /**
     * 获取编译时常量
     * @param paramStr EmptyString
     * @param callBack
     */
    void getConfig(String paramStr, GxbCallBack callBack);

    /**
     * 获取动态全局属性
     * @param paramStr Empty String
     * @param callBack
     */
    void getDynamicGlobalProperties(String paramStr, GxbCallBack callBack);

    /**
     * 获取全局属性
     * @param paramStr Empty String
     * @param callBack
     */
    void getGlobalProperties(String paramStr, GxbCallBack callBack);

    /**
     * 根据TXID查询交易，若交易超出有效期则会返回空值
     * @param paramStr <id>
     * @param callBack
     */
    void getRecentTransactionById(String paramStr, GxbCallBack callBack);

    /**
     * 获得交易信息
     * @param paramStr <trx>
     * @param callBack
     */
    void getTransaction(String paramStr, GxbCallBack callBack);
}
