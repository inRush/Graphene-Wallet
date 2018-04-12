package com.gxb.gxswallet.page.quotationdetail.provider;

import com.gxb.sdk.models.GXSExchange;

/**
 * @author inrush
 * @date 2018/3/26.
 */

public interface ExchangeProvider {
    /**
     * 获取交易信息
     *
     * @return {@link GXSExchange}
     */
    GXSExchange getExchange();
}
