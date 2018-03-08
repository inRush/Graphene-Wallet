package com.gxb.sdk.api.register.global;

/**
 * ע���ض����ݲ�ƷID�����ݽ��׻ص�
 * @author Wolkin
 *
 */
public class SetDataTransactionProductsSubscribeCallback extends GlobalAPI {

	@Override
	public void doParameter(String paraStr) {
		// TODO Auto-generated method stub
		String temStr = "";
		if(paraStr.contains(",")) {
			temStr = paraStr.replace(",", "\",\"");
		}else {
			temStr = paraStr;
		}
		
		this.jsonStr = "{" + 
				           "\"jsonrpc\": \"2.0\", " + 
				           "\"method\": \"call\", " + 
				           "\"params\": [0, \"set_data_transaction_products_subscribe_callback\", [[\"" + temStr + "\"]]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
