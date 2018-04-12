package com.gxb.sdk.wallet;

import com.gxb.sdk.wallet.graphene.chain.limit_order_object;
import com.gxb.sdk.wallet.graphene.chain.operations;

import java.util.List;

/**
 * Created by lorne on 22/09/2017.
 */
public class BitsharesNoticeMessage {
    int nSubscriptionId;

    // market_notice
    public List<operations.operation_type> listFillOrder;
    public List<limit_order_object> listOrderObject;

    // account
    public boolean bAccountChanged;
}
