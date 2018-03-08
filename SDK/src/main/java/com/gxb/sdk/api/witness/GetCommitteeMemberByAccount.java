package com.gxb.sdk.api.witness;

/**
 * 通过账户ID获取理事会成员信息
 * @author Wolkin
 *
 */
public class GetCommitteeMemberByAccount extends WitnessAPI {

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
							"\"params\": [0, \"get_committee_member_by_account\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
