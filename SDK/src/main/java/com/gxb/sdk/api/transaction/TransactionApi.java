package com.gxb.sdk.api.transaction;

import com.gxb.sdk.http.GxbCallBack;
import com.gxb.sdk.models.GXSExchange;

/**
 * @author inrush
 * @date 2018/3/15.
 */

public interface TransactionApi {
    /**
     * 获取交易信息
     *
     * @param callBack 回调
     */
    void getExchange(GxbCallBack callBack);

    /**
     * 获取指定交易对的交易信息
     *
     * @param exchange 交易对
     * @param callBack 回调
     */
    void getExchange(GXSExchange exchange, GxbCallBack callBack);

    /**
     * 获取指定交易对一定时间间隔间的交易信息
     *
     * @param exchange 交易对
     * @param interval 时间间隔
     * @param callBack 回调
     */
    void getExchange(GXSExchange exchange, int interval, GxbCallBack callBack);
}
