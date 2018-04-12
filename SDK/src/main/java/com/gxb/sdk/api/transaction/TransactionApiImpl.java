package com.gxb.sdk.api.transaction;

import com.gxb.sdk.Config;
import com.gxb.sdk.api.base.BaseApi;
import com.gxb.sdk.http.GxbCallBack;
import com.gxb.sdk.models.GXSExchange;

import java.util.Locale;

/**
 * @author inrush
 * @date 2018/3/15.
 */

public class TransactionApiImpl extends BaseApi implements TransactionApi {

    @Override
    public void getExchange(GxbCallBack callBack) {
        mHttpRequest.doGet(Config.EXCHANGE_URL.concat("/exchange"), callBack);
    }

    @Override
    public void getExchange(GXSExchange exchange, GxbCallBack callBack) {
        mHttpRequest.doGet(String.format("%s/exchange/%s/%s", Config.EXCHANGE_URL, exchange.getName(), exchange.getSymbol()), callBack);
    }

    @Override
    public void getExchange(GXSExchange exchange, int interval, GxbCallBack callBack) {
        mHttpRequest.doGet(
                String.format(Locale.CHINA,
                        "%s/kline/%s/%s?interval=%dm", Config.EXCHANGE_URL, exchange.getName(), exchange.getSymbol(), interval),
                callBack);
    }
}
