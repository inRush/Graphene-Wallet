package com.gxb.sdk.api.witness;

/**
 * 通过具体账户ID获得相关的被提议的交易
 * @author Wolkin
 *
 */
public class GetProposedTransactions extends WitnessAPI {

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
							"\"params\": [0, \"get_proposed_transactions\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
