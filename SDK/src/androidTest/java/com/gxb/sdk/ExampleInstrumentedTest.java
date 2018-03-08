package com.gxb.sdk;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.gxb.sdk.api.GxbApis;
import com.gxb.sdk.http.GxbCallBack;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() throws Exception {
        GxbApis.getInstance().walletApi()
                .getAccountByName("in-rush", new GxbCallBack() {
                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: " + result);
                    }
                });
    }
}
