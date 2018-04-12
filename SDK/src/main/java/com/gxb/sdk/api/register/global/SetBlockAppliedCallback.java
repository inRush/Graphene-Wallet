package com.gxb.sdk.api.register.global;

/**
 *注册区块是否被应用的回调
 * @author Wolkin
 *
 */
public class SetBlockAppliedCallback extends BaseGlobalAPI {

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
				           "\"params\": [0, \"set_block_applied_callback\", [[\"" + temStr + "\"]]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
