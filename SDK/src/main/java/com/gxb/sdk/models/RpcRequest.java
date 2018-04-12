package com.gxb.sdk.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * @author inrush
 * @date 2018/3/10.
 */

public class RpcRequest {
    @SerializedName("jsonrpc")
    public String jsonrpc;
    @SerializedName("method")
    public String method;
    @SerializedName("params")
    public Object[] params;
    @SerializedName("id")
    public int id;

    /**
     * 构建Request
     *
     * @param apiType      Api Type
     * @param apiName      Api Name
     * @param params       参数
     * @param paramIsArray 参数是否是数组
     * @return {@link RpcRequest}
     */
    public static String createRequest(int apiType, String apiName, Object[] params, boolean paramIsArray) {
        RpcRequest request = new RpcRequest();
        request.jsonrpc = "2.0";
        request.method = "call";
        request.params = new Object[]{apiType, apiName, paramIsArray ? new Object[]{params} : params};
        request.id = 1;
        return new Gson().toJson(request);
    }

    public static String createRequest(String apiName, Object[] params) {
        RpcRequest request = new RpcRequest();
        request.jsonrpc = "2.0";
        request.method = apiName;
        request.params = params;
        request.id = 1;
        return new Gson().toJson(request);
    }
}
