package com.gxb.sdk.api.base.paramconverters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 多参数转换器
 * @author inrush
 * @date 2018/2/14.
 */

public class MultipleParamConverter implements ParamConverter {
    private static MultipleParamConverter sConverter;

    private MultipleParamConverter() {

    }

    public static MultipleParamConverter getInstance() {
        if (sConverter == null) {
            synchronized (MultipleParamConverter.class) {
                if (sConverter == null) {
                    sConverter = new MultipleParamConverter();
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
                "\"params\": [" + apiType + ", \"" + apiName + "\", [[\"" + param + "\"]]], " +
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
