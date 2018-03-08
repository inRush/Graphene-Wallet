package com.gxb.sdk.api.block;

/**
 * 获取区块信息
 * @author Wolkin
 *
 */
public class GetBlock extends BaseBlockAPI {

	@Override
	public void doParameter(String paraStr) {
		String temStr = "";
		if(paraStr.contains(",")) {
			temStr = paraStr.replace(",", "\",\"");
		}else {
			temStr = paraStr;
		}
		this.jsonStr = "{" + 
				           "\"jsonrpc\": \"2.0\", " + 
				           "\"method\": \"call\", " + 
				           "\"params\": [0, \"get_block\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}
}
