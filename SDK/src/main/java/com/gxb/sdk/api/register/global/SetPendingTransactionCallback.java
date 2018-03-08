package com.gxb.sdk.api.register.global;

/**
 * ע��δȷ�ϵĽ��׵Ļص�
 * @author Wolkin
 *
 */
public class SetPendingTransactionCallback extends GlobalAPI {

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
				           "\"params\": [0, \"set_pending_transaction_callback\", [[\"" + temStr + "\"]]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
