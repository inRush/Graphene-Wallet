package com.gxb.sdk.api.history;

/**
 * 查询帐户的交易历史，其中start/stop为operation_history_id， id为1.11.x
 * @author Wolkin
 *
 */
public class GetAccountHistory extends HistoryAPI {

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
							"\"params\": [3, \"get_account_history\", [\"" + temStr + "\"]], " + 
							"\"id\":2" + 
						"}";
	}

}
