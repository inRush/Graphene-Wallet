package com.gxb.sdk.api.register.global;

import com.gxb.sdk.api.ApiObj;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * ����ȫ�ֽӿڳ����� ʵ��jsonObj()����
 * @author Wolkin
 *
 */

public abstract class GlobalAPI implements ApiObj {
	protected String jsonStr = "";
	
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
	public abstract void doParameter(String paraStr);

}
