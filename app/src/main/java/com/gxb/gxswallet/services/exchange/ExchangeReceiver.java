package com.gxb.gxswallet.services.exchange;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class ExchangeReceiver extends BroadcastReceiver {
    public static final String ACTION_CODE = "com.gxb.gxswallet.services.exchange";

    /**
     * 广播触发监听器
     */
    public interface OnReceiverListener {
        /**
         * 接收到广播时触发的监听器
         */
        void onReceiver();
    }

    private OnReceiverListener mListener;

    public ExchangeReceiver(OnReceiverListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_CODE.equals(intent.getAction())) {
            mListener.onReceiver();
        }
    }
}
