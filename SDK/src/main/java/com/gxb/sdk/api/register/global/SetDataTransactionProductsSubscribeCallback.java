package com.gxb.sdk.api.register.global;

/**
 *注册特定数据产品ID的数据交易回调
 * @author Wolkin
 *
 */
public class SetDataTransactionProductsSubscribeCallback extends BaseGlobalAPI {

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
