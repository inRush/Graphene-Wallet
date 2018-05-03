package com.gxb.sdk;

/**
 * @author inrush
 * @date 2018/3/15.
 */

public class Config {
    /**
     * 接口地址
     */
    public static final String RPC_URL_PROC = "https://node1.gxb.io/";
    public static final String FAUCET_URL = "https://opengateway.gxb.io";

    public static final String RPC_URL_DEV = "http://106.14.180.117:28090";
    public static final String RPC_URL_DEV_LOCAL_GXB = "http://192.168.42.73:8091";
    public static final String RPC_URL_DEV_LOCAL_BTS = "http://192.168.42.73:8092";
    /**
     * exchange 地址
     */
    public static final String EXCHANGE_URL = "https://walletgateway.gxb.io";

    public static String getRpcUrl() {
        if (BuildConfig.DEBUG) {
            return RPC_URL_DEV;
        } else {
            return RPC_URL_PROC;
        }
    }

}
