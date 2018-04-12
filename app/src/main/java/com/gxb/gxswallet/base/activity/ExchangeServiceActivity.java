package com.gxb.gxswallet.base.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.services.exchange.ExchangeService;
import com.sxds.common.app.BaseActivity;

/**
 * @author inrush
 * @date 2018/3/27.
 */

public abstract class ExchangeServiceActivity extends BaseActivity {
    private ServiceConnection mConnection;
    protected ExchangeService mExchangeService;

    protected void onConnectionSuccess() {

    }
    /**
     * 设置连接Service的Action
     *
     * @param intent 设置 action 的intent
     * @return 设置好Action的intent
     */
    protected abstract Intent setAction(Intent intent);

    @Override
    protected void onResume() {
        connectToExchangeService();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unConnectExchangeService();
        super.onPause();
    }

    /**
     * 连接Exchange Service
     */
    private void connectToExchangeService() {
        Intent intent = new Intent(App.getInstance(), ExchangeService.class);
        intent = setAction(intent);
        if (intent != null) {
            mConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    mExchangeService = ((ExchangeService.ExchangeBinder) iBinder).getService();
                    onConnectionSuccess();
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mExchangeService = null;
                }
            };
            bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
        }
    }

    /**
     * 取消服务的连接
     */
    private void unConnectExchangeService() {
        if (mConnection != null) {
            unbindService(mConnection);
            if(mExchangeService != null){
                mExchangeService.onDestroy();
                mExchangeService = null;
            }
        }
    }

}
