package com.gxb.sdk.api.block;

/**
 * 获取链属性
 * @author Wolkin
 *
 */
public class GetChainProperties extends BaseBlockAPI {

	@Override
	public void doParameter(String paraStr) {
		
		this.jsonStr = "{" + 
				           "\"jsonrpc\": \"2.0\", " + 
				           "\"method\": \"call\", " + 
				           "\"params\": [0, \"get_chain_properties\", []], " + 
				           "\"id\":1" + 
				       "}";
	}

}
