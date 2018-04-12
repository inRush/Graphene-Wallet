package com.gxb.gxswallet.receiver;

/**
 * 广播触发监听器
 *
 * @author inrush
 * @date 2018/3/24.
 */

public interface OnEventReceiverListener {
    /**
     * exchange list change
     */
    void onExchangeListChange();

    /**
     * exchange change
     */
    void onExchangeChange();

    /**
     * line data change
     */
    void onLineDataChange();
}
