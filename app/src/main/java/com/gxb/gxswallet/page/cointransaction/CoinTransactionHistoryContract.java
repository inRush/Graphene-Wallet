package com.gxb.gxswallet.page.cointransaction;

import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.cointransaction.model.TransactionHistory;
import com.sxds.common.presenter.BaseContract;

import java.util.List;

/**
 * @author inrush
 * @date 2018/4/4.
 */

public class CoinTransactionHistoryContract {
    interface View extends BaseContract.View<Presenter> {
        void onGetTransactionHistorySuccess(List<TransactionHistory> histories);
    }

    interface Presenter extends BaseContract.Presenter {
        void getTransactionHistory(WalletData wallet);
    }
}
