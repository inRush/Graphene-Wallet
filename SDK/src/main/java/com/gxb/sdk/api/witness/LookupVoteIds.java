package com.gxb.sdk.api.witness;

/**
 * 通过投票对象ID来获得投票对象
 * @author Wolkin
 *
 */
public class LookupVoteIds extends WitnessAPI {

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
							"\"params\": [0, \"lookup_vote_ids\", [[\"" + temStr + "\"]]], " + 
							"\"id\":1" + 
						"}";
	}
}
