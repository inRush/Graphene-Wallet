package com.gxb.sdk.api.base.paramconverters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 单参数转换器
 *
 * @author inrush
 * @date 2018/2/14.
 */

public class SingleParamConverter implements ParamConverter {
    private static SingleParamConverter sConverter;

    private SingleParamConverter() {

    }

    public static SingleParamConverter getInstance() {
        if (sConverter == null) {
            synchronized (SingleParamConverter.class) {
                if (sConverter == null) {
                    sConverter = new SingleParamConverter();
                }
            }
        }
        return sConverter;
    }

    @Override
    public JSONObject convertParam(int apiType, String apiName, String paramStr) {
        String param;
        if (paramStr.contains(",")) {
            param = paramStr.replace(",", "\",\"");
        } else {
            param = paramStr;
        }
        String jsonStr = "{" +
                "\"jsonrpc\": \"2.0\", " +
                "\"method\": \"call\", " +
                "\"params\": [" + apiType + ", \"" + apiName + "\", [\"" + param + "\"]], " +
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
