package com.gxb.sdk.api.baas;

import com.gxb.sdk.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author inrush
 * @date 2018/2/13.
 * Baas API
 */

public class BaasApiImpl implements BaasApi{
    private HttpRequest mHttpRequest;

    public BaasApiImpl(){
        mHttpRequest = new HttpRequest();
    }

    /**
     * 将处理完的json字符串转换为jsonObject
     * @param jsonStr 处理完成的json字符串
     * @return JSONObject
     */
    private JSONObject toJsonObject(String jsonStr){
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(jsonStr);
            System.out.println(jsonObj.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObj;
    }


    @Override
    public void getDataTransactionPayFeesByRequester(String paraStr) {

    }

    @Override
    public void getDataTransactionProductCostsByProductId(String paraStr) {

    }

    @Override
    public void getDataTransactionProductCostsByRequester(String paraStr) {

    }

    @Override
    public void getDataTransactionTotalCountByProductId(String paraStr) {

    }

    @Override
    public void getDataTransactionTotalCountByRequester(String paraStr) {

    }
}
