package com.gxb.gxswallet.page.main;

import com.gxb.gxswallet.services.exchange.ExchangeService;

/**
 * @author inrush
 * @date 2018/3/25.
 */

public interface ExchangeServiceProvider {
    ExchangeService getService();
}
