package com.gxb.sdk.http;

import com.parkingwang.okhttp3.LogInterceptor.LogInterceptor;

import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author inrush
 * @date 2018/2/13.
 */

public class HttpRequest {
    private static final String TAG = "HttpRequest";
    private OkHttpClient mClient;
    /**
     * 接口地址
     */
    private static final String RPC_URL_PROC = "https://node1.gxb.io/";
    private static final String FAUCET_URL = "https://opengateway.gxb.io";

    private static final String RPC_URL_DEV = "106.14.180.117:28090";

    public HttpRequest() {
        // 忽略ssl证书
        mClient = new OkHttpClient.Builder()
                .sslSocketFactory(getSSLSocketFactory())
                .hostnameVerifier(getHostnameVerifier())
                .addInterceptor(new LogInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

    }

    /**
     * 对接口进行请求
     *
     * @param jsonObject 参数
     * @param callBack   回调
     */
    public void doCallRpc(JSONObject jsonObject, final GxbCallBack callBack) {
        final Request request = createRequest(RPC_URL_PROC, jsonObject);
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    callBack.onSuccess(result);
                }
            }
        });
    }

    /**
     * 各个接口自定义创建Request
     *
     * @param url        接口地址
     * @param jsonObject 参数
     * @return Request
     */
    protected Request createRequest(String url, JSONObject jsonObject) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        return request;
    }

    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    /**
     * 获取TrustManager
     *
     * @return
     */
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }

    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
