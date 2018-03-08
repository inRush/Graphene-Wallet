package com.gxb.sdk.api.witness;

/**
 * 验证交易是否已满足全部签名要求
 * @author Wolkin
 *
 */
public class VerifyAuthority extends WitnessAPI {

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
							"\"params\": [0, \"verify_authority\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}
}
