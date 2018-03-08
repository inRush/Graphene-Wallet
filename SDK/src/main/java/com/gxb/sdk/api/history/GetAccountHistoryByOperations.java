package com.gxb.sdk.api.history;

/**
 * 查询帐户的交易历史，根据operations_ids筛选指定类型的交易历史，其中start为序号，从1开始
 * @author Wolkin
 *
 */
public class GetAccountHistoryByOperations extends HistoryAPI {

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
							"\"params\": [3, \"get_account_history_by_operations\", [\"" + temStr + "\"]], " + 
							"\"id\":1" + 
						"}";
	}

}
