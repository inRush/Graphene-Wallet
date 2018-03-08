package com.gxb.sdk.api.wallet;

/**
 * 获取账户account_id相关的账户id
 * @author Wolkin
 *
 */
public class GetAccountReferences extends BaseWalletAPI {

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
							"\"params\": [0, \"get_account_references\", [\"" + temStr + "\"]], " + 
							"\"id\":1" + 
						"}";
	}

}
