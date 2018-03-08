package com.gxb.sdk.api.block;

/**
 * 获得交易信息
 * @author Wolkin
 *
 */
public class GetTransaction extends BaseBlockAPI {

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
				           "\"params\": [0, \"get_transaction\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}
}
