package com.gxb.sdk.api.register.global;

/**
 * ��ȡָ��ʱ�������ݽ��׵�Ӷ��
 * @author Wolkin
 *
 */
public class GetDataTransactionCommission extends GlobalAPI {

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
