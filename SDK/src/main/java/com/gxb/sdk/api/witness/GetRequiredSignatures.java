package com.gxb.sdk.api.witness;

/**
 * 获取签名的交易信息的签名公钥
 * @author Wolkin
 *
 */
public class GetRequiredSignatures extends WitnessAPI {

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
							"\"params\": [0, \"get_required_signatures\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}
}
