package com.gxb.gxswallet.services.rpc;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author inrush
 * @date 2018/4/28.
 */

public class RetryWithDelay implements Function<Observable<Throwable>, ObservableSource<?>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            if (throwable instanceof UnknownHostException) {
                return Observable.error(throwable);
            }
            if (++retryCount <= maxRetries) {
                return Observable.timer(retryDelayMillis,
                        TimeUnit.MILLISECONDS);
            }
            return Observable.error(throwable);
        });
    }
}
