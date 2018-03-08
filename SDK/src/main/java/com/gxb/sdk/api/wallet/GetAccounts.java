package com.gxb.sdk.api.wallet;

/**
 * 账户查询
 * @author Wolkin
 *
 */
public class GetAccounts extends BaseWalletAPI {

	/**
	 * 当个账户查询以逗号分隔
	 */
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
							"\"params\": [0, \"get_accounts\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}
}
