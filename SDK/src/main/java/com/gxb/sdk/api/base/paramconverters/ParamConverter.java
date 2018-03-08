package com.gxb.sdk.api.base.paramconverters;

import org.json.JSONObject;

/**
 * 参数转换器
 *
 * @author inrush
 * @date 2018/2/14.
 */

public interface ParamConverter {
    JSONObject convertParam(int apiType, String apiName,String paramStr);
}
