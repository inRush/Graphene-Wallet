package com.gxb.sdk.api.baas;

/**
 * 获取在指定时间内购买指定产品的产品费用
 * @author Wolkin
 *
 */
public class GetDataTransactionProductCostsByProductId extends BaseBaasApi {

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
							"\"params\": [0, \"get_data_transaction_product_costs_by_product_id\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
