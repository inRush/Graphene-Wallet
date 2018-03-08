package com.gxb.sdk.api.wallet;

/**
 * 获取资产账户信息
 * @author Wolkin
 *
 */
public class GetAccountBalances extends BaseWalletAPI {

	@Override
	public void doParameter(String paraStr) {
		// TODO Auto-generated method stub
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
				           "\"params\": [0, \"get_account_balances\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
