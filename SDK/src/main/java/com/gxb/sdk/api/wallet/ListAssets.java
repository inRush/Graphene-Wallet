package com.gxb.sdk.api.wallet;

/**
 * 通过资产符号名称获取资产信息，并返回前limit个
 * @author Wolkin
 *
 */
public class ListAssets extends BaseWalletAPI {

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
							"\"params\": [0, \"list_assets\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
