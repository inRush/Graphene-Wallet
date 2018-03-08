package com.gxb.sdk.api.block;

/**
 * 根据TXID查询交易，若交易超出有效期则会返回空值
 * @author Wolkin
 *
 */
public class GetRecentTransactionById extends BaseBlockAPI {

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
				           "\"params\": [0, \"get_recent_transaction_by_id\", [\"" + temStr + "\"]], " + 
				           "\"id\":1" + 
				       "}";
	}

}
