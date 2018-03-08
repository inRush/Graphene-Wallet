package com.gxb.sdk.api.witness;

/**
 * 获取签名的交易信息的十六进制编码
 * @author Wolkin
 *
 */
public class GetTransactionHex extends WitnessAPI {

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
							"\"params\": [0, \"get_transaction_hex\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
