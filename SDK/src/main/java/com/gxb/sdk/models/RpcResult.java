package com.gxb.sdk.models;

/**
 * Rpc 统一返回结构
 * @author inrush
 * @date 2018/3/10.
 */

public class RpcResult<D> {
    public int id;
    public String jsonRpc;
    public D result;
}
