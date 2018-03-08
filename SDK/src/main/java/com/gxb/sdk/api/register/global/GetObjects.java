package com.gxb.sdk.api.register.global;

/**
 * ����ID��ѯĿ�����
 * @author Wolkin
 *
 */

public class GetObjects extends GlobalAPI {

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
				           "\"params\": [0, \"get_objects\", [[\"" + temStr + "\"]]], " + 
				           "\"id\":1" + 
				       "}";
	}
	
}
