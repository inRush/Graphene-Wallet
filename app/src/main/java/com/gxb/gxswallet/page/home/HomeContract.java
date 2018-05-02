package com.gxb.gxswallet.page.home;

import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.sxds.common.presenter.BaseContract;

import java.util.HashMap;
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
         * @param balances 余额
         * @param wallet  钱包
         */
        void onFetchWalletBalanceSuccess(WalletData wallet, HashMap<String, Double> balances) ;
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
        List<AssetData> fetchSupportCoin();

        List<CoinItem> convertToCoinItem(List<AssetData> coinDatas);
    }
}
