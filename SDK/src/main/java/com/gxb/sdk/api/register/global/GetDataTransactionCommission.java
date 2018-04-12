package com.gxb.sdk.api.register.global;

/**
 * 获取指定时间内数据交易的佣金
 * @author Wolkin
 *
 */
public class GetDataTransactionCommission extends BaseGlobalAPI {

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
							"\"params\": [0, \"get_data_transaction_commission\", [\"" + temStr + "\"]], " + 
							"\"id\":1" + 
						"}";
	}

}
