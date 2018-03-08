package com.gxb.sdk.api.baas;

/**
 * Baas API 接口定义
 * @author inrush
 * @date 2018/2/13.
 */

public interface BaasApi {
    /**
     * 获取请求账户（即商户）在指定时间内发起数据交易的手续费
     * @param paraStr
     */
    void getDataTransactionPayFeesByRequester(String paraStr);

    /**
     * 获取在指定时间内购买指定产品的产品费用
     * @param paraStr
     */
    void getDataTransactionProductCostsByProductId(String paraStr);

    /**
     * 获取请求账户（即商户）在指定时间内数据交易产生的产品费用
     * @param paraStr
     */
    void getDataTransactionProductCostsByRequester(String paraStr);

    /**
     * 获取在指定时间内购买指定产品的次数
     * @param paraStr
     */
    void getDataTransactionTotalCountByProductId(String paraStr);

    /**
     * 获取请求账户（即商户）在指定时间内发起数据交易的次数
     * @param paraStr
     */
    void getDataTransactionTotalCountByRequester(String paraStr);
}
