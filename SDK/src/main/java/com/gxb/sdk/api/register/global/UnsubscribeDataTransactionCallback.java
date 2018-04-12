package com.gxb.sdk.api.register.global;

/**
 * 取消注册数据交易的回调
 * @author Wolkin
 *
 */
public class UnsubscribeDataTransactionCallback extends BaseGlobalAPI {

	@Override
	public void doParameter(String paraStr) {
		// TODO Auto-generated method stub
		this.jsonStr = "{" + 
				           "\"jsonrpc\": \"2.0\", " + 
				           "\"method\": \"call\", " + 
				           "\"params\": [0, \"unsubscribe_data_transaction_callback\", [[]]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
