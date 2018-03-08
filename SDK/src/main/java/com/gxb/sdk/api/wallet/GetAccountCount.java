package com.gxb.sdk.api.wallet;

/**
 * 获取链上注册的所有账户数量
 * @author Wolkin
 *
 */
public class GetAccountCount extends BaseWalletAPI {

	@Override
	public void doParameter(String paraStr) {
		this.jsonStr = "{" + 
				           "\"jsonrpc\": \"2.0\", " + 
				           "\"method\": \"call\", " + 
				           "\"params\": [0, \"get_account_count\", []], " + 
				           "\"id\":1" + 
				       "}";
	}
}
