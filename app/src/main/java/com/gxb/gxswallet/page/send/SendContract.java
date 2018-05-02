package com.gxb.gxswallet.page.send;

import com.google.common.base.Function;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.sxds.common.presenter.BaseContract;

import org.bitcoinj.core.ECKey;

import java.util.List;


/**
 * @author inrush
 * @date 2018/3/16.
 */

public class SendContract {
    interface View extends BaseContract.View<Presenter> {
        /**
         * 发送成功后的回调
         */
        void onSendSuccess();

        /**
         * 查询手续费成功
         *
         * @param fee 手续费
         */
        void onQueryFeeSuccess(double fee, Function<Boolean, Void> callback);

        /**
         * 获取账户余额成功回调
         *
         * @param balance 余额
         * @param wallet  钱包
         */
        void onFetchWalletBalanceSuccess(WalletData wallet, double balance);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 发送
         *
         * @param from   账户名称(地址)
         * @param amount 数量
         */
        void send(ECKey privateKey, String from, String to, String amount, AssetData assetData, String memo);


        /**
         * 获取所有钱包
         *
         * @return 钱包列表
         */
        List<WalletData> fetchWallet();

        /**
         * 获取钱包的余额
         *
         * @param wallet 钱包
         */
        void fetchWalletBalance(WalletData wallet, AssetData asset);
    }
}
