package com.gxb.sdk.api.register;

import com.gxb.sdk.api.ApiObj;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * 定义Database类，注册获取公信链database api编号
 * @author Wolkin
 *
 */
public class Database implements ApiObj {
	private String jsonStr = "";
	
	@Override
	public JSONObject jsonObj() {
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(this.jsonStr);
			System.out.println(jsonObj.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj;
	}

	@Override
	public void doParameter(String paraStr) {
		// TODO Auto-generated method stub
		this.jsonStr = "{" + 
							"\"jsonrpc\": \"2.0\", " + 
							"\"method\": \"call\", " + 
							"\"params\": [1, \"database\", []], " + 
							"\"id\":2" + 
						"}";
	}

}
