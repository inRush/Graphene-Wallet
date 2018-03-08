package com.gxb.sdk.api.block;

/**
 * 获取链ID
 * @author Wolkin
 *
 */
public class GetChainId extends BaseBlockAPI {

	@Override
	public void doParameter(String paraStr) {
		this.jsonStr = "{" + 
				           "\"jsonrpc\": \"2.0\", " + 
				           "\"method\": \"call\", " + 
				           "\"params\": [0, \"get_chain_id\", []], " + 
				           "\"id\":1" + 
				       "}";
	}

}
