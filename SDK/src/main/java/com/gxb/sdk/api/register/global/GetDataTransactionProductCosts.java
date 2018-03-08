package com.gxb.sdk.api.register.global;

/**
 * ��ȡָ��ʱ�������ݽ��׵Ĳ�Ʒ����
 * @author Wolkin
 *
 */
public class GetDataTransactionProductCosts  extends GlobalAPI{

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
				           "\"params\": [0, \"get_data_transaction_product_costs\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
