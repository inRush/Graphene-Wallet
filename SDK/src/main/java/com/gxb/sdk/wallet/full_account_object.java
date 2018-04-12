package com.gxb.sdk.wallet;


import com.gxb.sdk.wallet.graphene.chain.limit_order_object;

import java.util.List;


public class full_account_object {
    public account_object account;
    public List<limit_order_object> limit_orders;
    public List<account_balance_object> balances;
}
