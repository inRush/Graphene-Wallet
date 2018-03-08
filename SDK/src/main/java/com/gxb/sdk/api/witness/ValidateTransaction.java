package com.gxb.sdk.api.witness;

/**
 * 在当前情况下验证交易而不广播交易
 * @author Wolkin
 *
 */
public class ValidateTransaction extends WitnessAPI {

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
							"\"params\": [0, \"validate_transaction\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}
}
