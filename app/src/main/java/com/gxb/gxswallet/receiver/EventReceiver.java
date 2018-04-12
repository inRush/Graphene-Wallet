package com.gxb.gxswallet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author inrush
 * @date 2018/3/24.
 */

public class EventReceiver extends BroadcastReceiver {

    private OnEventReceiverListener mListener;

    public EventReceiver(OnEventReceiverListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case EventActionCode.ACTION_CODE_EXCHANGE_LIST_CHANGE:
                    mListener.onExchangeListChange();
                    break;
                case EventActionCode.ACTION_CODE_EXCHANGE_CHANGE:
                    mListener.onExchangeChange();
                    break;
                case EventActionCode.ACTION_CODE_LINE_DATA_CHANGE:
                    mListener.onLineDataChange();
                    break;
                default:
                    break;
            }
        }

    }


}
