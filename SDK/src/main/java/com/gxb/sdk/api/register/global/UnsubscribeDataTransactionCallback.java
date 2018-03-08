package com.gxb.sdk.api.register.global;

/**
 * ȡ��ע�����ݽ��׵Ļص�
 * @author Wolkin
 *
 */
public class UnsubscribeDataTransactionCallback extends GlobalAPI {

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
