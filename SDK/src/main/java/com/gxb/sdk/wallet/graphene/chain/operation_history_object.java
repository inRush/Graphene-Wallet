package com.gxb.sdk.wallet.graphene.chain;



public class operation_history_object {
//    @ColumnInfo(name = "history_id")
    public String id;
    public operations.operation_type op;
    public int block_num;
    public int trx_in_block;
    public int op_in_trx;
    public int virtual_op;

}
