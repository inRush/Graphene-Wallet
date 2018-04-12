package com.gxb.sdk.http;

/**
 * Gxb 接口回调
 *
 * @author inrush
 * @date 2018/2/13.
 */
public interface GxbCallBack {
    /**
     * 失败回调
     */
    void onFailure(Error error);

    /**
     * 成功回调
     *
     * @param result 结果字符串
     */
    void onSuccess(String result);
}
