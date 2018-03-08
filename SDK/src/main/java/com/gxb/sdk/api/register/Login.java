package com.gxb.sdk.api.register;

import com.gxb.sdk.api.ApiObj;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 定义Login类，获取公信链Login编号
 * @author Wolkin
 *
 */
public class Login implements ApiObj {
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
		this.jsonStr = "{" + 
							"\"jsonrpc\": \"2.0\", " + 
							"\"method\": \"call\", " + 
							"\"params\": [1, \"login\", [\"\",\"\"]], " + 
							"\"id\":1" + 
					   "}";
	}
	
}
