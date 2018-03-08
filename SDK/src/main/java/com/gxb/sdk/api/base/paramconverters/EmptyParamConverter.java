package com.gxb.sdk.api.base.paramconverters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public class EmptyParamConverter implements ParamConverter {
    private static EmptyParamConverter sConverter;

    private EmptyParamConverter() {

    }

    public static EmptyParamConverter getInstance() {
        if (sConverter == null) {
            synchronized (EmptyParamConverter.class) {
                if (sConverter == null) {
                    sConverter = new EmptyParamConverter();
                }
            }
        }
        return sConverter;
    }

    @Override
    public JSONObject convertParam(int apiType, String apiName, String paramStr) {
        String jsonStr = "{" +
                "\"jsonrpc\": \"2.0\", " +
                "\"method\": \"call\", " +
                "\"params\": [" + apiType + ", \"" + apiName + "\", []], " +
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
