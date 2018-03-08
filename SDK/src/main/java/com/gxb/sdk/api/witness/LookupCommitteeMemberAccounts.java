package com.gxb.sdk.api.witness;

/**
 * 获得已注册理事会成员的ID和账户名,并返回前limit个
 * @author Wolkin
 *
 */
public class LookupCommitteeMemberAccounts extends WitnessAPI {

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
							"\"params\": [0, \"lookup_committee_member_accounts\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}

}
