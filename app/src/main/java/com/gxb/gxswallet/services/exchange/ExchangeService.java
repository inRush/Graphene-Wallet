package com.gxb.gxswallet.services.exchange;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gxb.gxswallet.receiver.EventActionCode;
import com.gxb.sdk.api.GxbApis;
import com.gxb.sdk.http.GxbCallBack;
import com.gxb.sdk.models.GXSExchange;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author inrush
 * @date 2018/3/22.
 */

public class ExchangeService extends Service {
    private boolean isExchangeFetchLoop = false;
    private List<GXSExchange> mGxsExchanges = new ArrayList<>();
    private GXSExchange mGXSExchange;
    private double mBestPriceRmb = 0;
    private double mBestPriceUsd = 0;
    private ExchangeBinder mBinder = new ExchangeBinder(this);

    /**
     * 运行模式
     */
    public static final String RUN_TYPE_KEY = "run_type";
    public static final String RUN_TYPE_FETCH_LIST = "fetch_list";
    public static final String RUN_TYPE_FETCH_ONE = "fetch_one";

    public static final String EXCHANGE_KEY = "exchange";

    /**
     * 通知变化
     */
    private void noticeChange(String actionCode) {
        Intent intent = new Intent();
        intent.setAction(actionCode);
        sendBroadcast(intent);
    }

    /**
     * 执行循环
     *
     * @param interval 时间间隔
     * @param callback 回调
     */
    private void runLoop(final int interval, final Action callback) {
        Run.onBackground(new Action() {
            @Override
            public void call() {
                while (isExchangeFetchLoop) {
                    callback.call();
                    try {
                        // 每interval毫秒获取一次更新
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    /**
     * 开始获取exchange循环
     */
    public void startFetchExchangeListLoop() {
        runLoop(3000, new Action() {
            @Override
            public void call() {
                GxbApis.getInstance()
                        .transactionApi().getExchange(new GxbCallBack() {
                    @Override
                    public void onFailure(Error error) {
                        error.printStackTrace();
                    }

                    @Override
                    public void onSuccess(String result) {
                        mGxsExchanges = GXSExchange.fromJsonToList(result);
                        double bestPriceRmb = 0;
                        double bestPriceUsd = 0;
                        // 计算所有价格中最好的价格
                        for (GXSExchange exchange : mGxsExchanges) {
                            double priceRmb = Double.parseDouble(exchange.getPrice_rmb());
                            double priceUsd = Double.parseDouble(exchange.getPrice_dollar());
                            if (priceRmb > bestPriceRmb) {
                                bestPriceRmb = priceRmb;
                            }
                            if (priceUsd > bestPriceUsd) {
                                bestPriceUsd = priceUsd;
                            }
                        }
                        mBestPriceRmb = bestPriceRmb;
                        mBestPriceUsd = bestPriceUsd;
                        noticeChange(EventActionCode.ACTION_CODE_EXCHANGE_LIST_CHANGE);
                    }
                });
            }
        });
    }

    private void startFetchKLineLoop(final GXSExchange exchange) {
        runLoop(3000, new Action() {
            @Override
            public void call() {
                GxbApis.getInstance()
                        .transactionApi().getExchange(exchange, new GxbCallBack() {
                    @Override
                    public void onFailure(Error error) {
                        error.printStackTrace();
                    }

                    @Override
                    public void onSuccess(String result) {
                        mGXSExchange = GXSExchange.fromJson(result);
                        noticeChange(EventActionCode.ACTION_CODE_EXCHANGE_CHANGE);
                    }
                });
            }
        });
    }


    public void stopFetchExchangeListLoop() {
        isExchangeFetchLoop = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String runType = intent.getStringExtra(RUN_TYPE_KEY);
        if (!isExchangeFetchLoop) {
            isExchangeFetchLoop = true;
            switch (runType) {
                case RUN_TYPE_FETCH_LIST:
                    startFetchExchangeListLoop();
                    break;
                case RUN_TYPE_FETCH_ONE:
                    GXSExchange exchange = intent.getParcelableExtra(EXCHANGE_KEY);
                    startFetchKLineLoop(exchange);
                    break;
                default:
                    break;
            }
        }
        return mBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopFetchExchangeListLoop();
    }

    public class ExchangeBinder extends Binder {
        private WeakReference<ExchangeService> mServiceWeakReference;

        ExchangeBinder(ExchangeService service) {
            mServiceWeakReference = new WeakReference<>(service);
        }

        public ExchangeService getService() {
            return mServiceWeakReference.get();
        }
    }

    public List<GXSExchange> getGxsExchanges() {
        return mGxsExchanges;
    }

    public double getBestPriceRmb() {
        return mBestPriceRmb;
    }

    public double getBestPriceUsd() {
        return mBestPriceUsd;
    }

    public GXSExchange getGXSExchange() {
        return mGXSExchange;
    }
}
