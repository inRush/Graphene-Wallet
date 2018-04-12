package com.gxb.sdk.market;


import com.gxb.sdk.wallet.graphene.chain.asset_object;
import com.gxb.sdk.wallet.graphene.chain.limit_order_object;

public class OpenOrder {
    public limit_order_object limitOrder;
    public asset_object base;
    public asset_object quote;
    public double price;
}
