package com.gxb.sdk.api.register.global;

import com.gxb.sdk.http.GxbCallBack;

/**
 * @author inrush
 * @date 2018/3/12.
 */

public interface GlobalApi {
    /**
     * 通过账户名获取账户信息
     * @param params <account_names>
     * @param callBack
     */
    void getObjects(Object[] params, GxbCallBack callBack);
}
