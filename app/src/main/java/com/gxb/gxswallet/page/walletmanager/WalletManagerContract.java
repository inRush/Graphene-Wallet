package com.gxb.gxswallet.page.walletmanager;

import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.sxds.common.presenter.BaseContract;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/27.
 */

public class WalletManagerContract {
    interface View extends BaseContract.View<Presenter> {
        /**
         * 获取账户余额成功回调
         *
         * @param balance 余额
         * @param wallet  钱包
         */
        void onFetchWalletBalanceSuccess(WalletData wallet, double balance);
    }

    interface Presenter extends BaseContract.Presenter {
        List<WalletData> fetchWallets();
        void fetchWalletBalance(WalletData wallet, AssetData asset);

    }
}
