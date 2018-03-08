package com.gxb.sdk.api.baas;

/**
 * 获取请求账户（即商户）在指定时间内数据交易产生的产品费用
 * @author Wolkin
 *
 */
public class GetDataTransactionProductCostsByRequester extends BaseBaasApi {

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
							"\"params\": [0, \"get_data_transaction_product_costs_by_requester\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
