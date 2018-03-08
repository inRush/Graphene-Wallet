package com.gxb.sdk.api.witness;

/**
 * 通过开始和结束时间获取被投诉的数据源，并返回前limit个
 * @author Wolkin
 *
 */
public class ListDataTransactionComplainDatasources extends WitnessAPI {

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
				           "\"params\": [0, \"list_data_transaction_complain_datasources\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
