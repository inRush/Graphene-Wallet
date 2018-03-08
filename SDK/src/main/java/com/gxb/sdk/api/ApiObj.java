package com.gxb.sdk.api;

import org.json.JSONObject;

/**
 * Describe：定义API接口对象
 * @author Wolkin
 * 
 */

public interface ApiObj {
	
	/**
	 * 打包各个接口信息
	 * @return JSONObject
	 */
    JSONObject jsonObj();
	
	/**
	 * 各个接口参数处理
	 * @param paraStr 参数
	 */
	void doParameter(String paraStr);
}
