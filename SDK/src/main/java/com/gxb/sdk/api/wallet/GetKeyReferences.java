package com.gxb.sdk.api.wallet;

/**
 * 返回所有指向公钥的帐户信息	
 * @author Wolkin
 *
 */
public class GetKeyReferences extends BaseWalletAPI {

	@Override
	public void doParameter(String paraStr) {
		//多参数以逗号分隔的识别
		String temStr = "";
		if(paraStr.contains(",")) {
			temStr = paraStr.replace(",", "\",\"");
		}else {
			temStr = paraStr;
		}
		
		this.jsonStr = "{" + 
							"\"jsonrpc\": \"2.0\", " + 
							"\"method\": \"call\", " + 
							"\"params\": [0, \"get_key_references\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
