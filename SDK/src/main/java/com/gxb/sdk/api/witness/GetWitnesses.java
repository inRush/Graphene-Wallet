package com.gxb.sdk.api.witness;

/**
 * 通过见证人ID获取见证人列表
 * @author Wolkin
 *
 */
public class GetWitnesses extends WitnessAPI {

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
							"\"params\": [0, \"get_witnesses\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
