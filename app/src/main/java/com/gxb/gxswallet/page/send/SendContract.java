package com.gxb.gxswallet.page.send;

import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.sxds.common.presenter.BaseContract;

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

        void onQueryFeeSuccess(double fee);

        /**
         * 获取账户余额成功回调
         *
         * @param balance 余额
         * @param wallet  钱包
         */
        void onFetchWalletBalanceSuccess(WalletData wallet, List<AccountBalance> balance);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 发送
         *
         * @param from   账户名称(地址)
         * @param amount 数量
         */
        void send(String from, String to, String amount, String coin, String memo);

        void queryFee(String from, String to, String amount, String coin, String memo);

        List<WalletData> fetchWallet();

        void fetchWalletBalance(final WalletData wallet);
    }
}
