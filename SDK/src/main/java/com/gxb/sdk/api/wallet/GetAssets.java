package com.gxb.sdk.api.wallet;

/**
 * 通过资产ID获取资产
 * @author Wolkin
 *
 */
public class GetAssets extends BaseWalletAPI {

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
							"\"params\": [0, \"get_assets\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
