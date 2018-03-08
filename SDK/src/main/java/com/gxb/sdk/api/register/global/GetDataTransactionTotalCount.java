package com.gxb.sdk.api.register.global;

/**
 * ��ȡָ��ʱ�������ݽ��׵Ĵ���
 * @author Wolkin
 *
 */
public class GetDataTransactionTotalCount extends GlobalAPI {

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
				           "\"params\": [0, \"get_data_transaction_total_count\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
