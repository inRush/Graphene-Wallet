package com.gxb.gxswallet.page.home;

import com.gxb.gxswallet.db.coin.CoinData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.sxds.common.presenter.BaseContract;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/14.
 */

class HomeContract {
    interface View extends BaseContract.View<Presenter> {
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
         * 获取账户余额
         *
         * @param wallet 钱包
         */
        void fetchWalletBalance(WalletData wallet);

        /**
         * 获取支持的币种
         */
        List<CoinData> fetchSupportCoin();

        List<CoinItem> convertToCoinItem(List<CoinData> coinDatas);
        /**
         * 获取本地导入的钱包
         *
         * @return {@link WalletData}
         */
        List<WalletData> fetchWallet();
    }
}
