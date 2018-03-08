package com.gxb.sdk.api.witness;

/**
 * 验证签名人是否有足够的权力控制一个帐户
 * @author Wolkin
 *
 */
public class VerifyAccountAuthority extends WitnessAPI {

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
							"\"params\": [0, \"verify_account_authority\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
