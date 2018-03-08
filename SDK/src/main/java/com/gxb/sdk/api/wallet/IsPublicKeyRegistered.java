package com.gxb.sdk.api.wallet;

/**
 * 验证公钥是否已经被注册
 * @author Wolkin
 *
 */
public class IsPublicKeyRegistered extends BaseWalletAPI {

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
							"\"params\": [0, \"is_public_key_registered\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
