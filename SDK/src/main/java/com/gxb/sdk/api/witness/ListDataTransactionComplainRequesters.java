package com.gxb.sdk.api.witness;

/**
 * 通过开始和结束时间获取投诉的发起人，并返回前limit个
 * @author Wolkin
 *
 */
public class ListDataTransactionComplainRequesters extends WitnessAPI {

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
				           "\"params\": [0, \"list_data_transaction_complain_requesters\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
