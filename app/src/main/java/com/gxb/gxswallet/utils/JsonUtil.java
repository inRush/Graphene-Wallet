package com.gxb.gxswallet.utils;

import com.google.gson.Gson;

/**
 * @author inrush
 * @date 2018/3/12.
 */

public class JsonUtil {
    private static Gson sGson;

    public static Gson getGson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
    }
}
