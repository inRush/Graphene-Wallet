package com.gxb.sdk.api.wallet;

/**
 * 通过资产符号获取资产列表
 * @author Wolkin
 *
 */
public class LookupAssetSymbols extends BaseWalletAPI {

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
							"\"params\": [0, \"lookup_asset_symbols\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
