package com.gxb.sdk.api.wallet;

/**
 * 返回地址address上所有未领取的余额对象
 * @author Wolkin
 *
 */
public class GetBalanceObjects extends BaseWalletAPI {

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
				           "\"params\": [0, \"get_balance_objects\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
