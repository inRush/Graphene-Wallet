package com.gxb.sdk.api.witness;

/**
 * 获取已注册见证人的数量
 * @author Wolkin
 *
 */
public class GetWitnessCount extends WitnessAPI {

	@Override
	public void doParameter(String paraStr) {
		this.jsonStr = "{" + 
							"\"jsonrpc\": \"2.0\", " + 
							"\"method\": \"call\", " + 
							"\"params\": [0, \"get_witness_count\", []], " + 
							"\"id\":1" + 
						"}";
	}

}
