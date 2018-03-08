package com.gxb.sdk.api.base;

import com.gxb.sdk.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public abstract class BaseApi {
    protected HttpRequest mHttpRequest;

    protected BaseApi() {
        mHttpRequest = new HttpRequest();
    }

    /**
     * 创建参数json
     *
     * @param apiType api类型
     * @param apiName api指令名称
     * @param para    参数
     * @return JsonObject
     */
    protected JSONObject createParam(int apiType, String apiName, String para) {
        String jsonStr = "{" +
                "\"jsonrpc\": \"2.0\", " +
                "\"method\": \"call\", " +
                "\"params\": [" + apiType + ", \"" + apiName + "\", [" + para + "]], " +
                "\"id\":1" +
                "}";
        JSONObject object = null;
        try {
            object = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
