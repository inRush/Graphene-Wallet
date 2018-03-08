package com.gxb.sdk.api.wallet;

/**
 * 获取已注册账户的账户名和ID
 * @author Wolkin
 *
 */
public class LookupAccounts extends BaseWalletAPI {

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
				           "\"params\": [0, \"lookup_accounts\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
