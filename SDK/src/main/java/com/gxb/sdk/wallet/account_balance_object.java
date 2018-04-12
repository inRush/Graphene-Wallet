package com.gxb.sdk.wallet;


import com.gxb.sdk.wallet.graphene.chain.asset_object;
import com.gxb.sdk.wallet.graphene.chain.object_id;

/**
 * Created by lorne on 01/11/2017.
 */

public class account_balance_object {
    public object_id id;
    public object_id<account_object> owner;
    public object_id<asset_object> asset_type;
    public long balance;
}
