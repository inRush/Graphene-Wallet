package com.gxb.sdk.api.witness;

/**
 * 通过操作ID和资产ID获取手续费
 * @author Wolkin
 *
 */
public class GetRequiredFees extends WitnessAPI {

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
							"\"params\": [0, \"get_required_fees\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
