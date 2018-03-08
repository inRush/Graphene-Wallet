package com.gxb.sdk.api.witness;

/**
 * 通过账户ID获取工作对象信息
 * @author Wolkin
 *
 */
public class GetWorkersByAccount extends WitnessAPI {

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
							"\"params\": [0, \"get_workers_by_account\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}
}
