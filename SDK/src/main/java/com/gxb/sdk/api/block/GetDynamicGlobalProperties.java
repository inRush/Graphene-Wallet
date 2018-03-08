package com.gxb.sdk.api.block;

/**
 * 获取动态全局属性
 * @author Wolkin
 *
 */
public class GetDynamicGlobalProperties extends BaseBlockAPI {

	@Override
	public void doParameter(String paraStr) {
		this.jsonStr = "{" + 
				           "\"jsonrpc\": \"2.0\", " + 
				           "\"method\": \"call\", " + 
				           "\"params\": [0, \"get_dynamic_global_properties\", []], " + 
				           "\"id\":1" + 
				       "}";
	}
}
