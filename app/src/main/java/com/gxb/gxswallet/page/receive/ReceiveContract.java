package com.gxb.gxswallet.page.receive;

import com.gxb.gxswallet.db.coin.CoinData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.sxds.common.presenter.BaseContract;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/17.
 */

public class ReceiveContract {
    interface View extends BaseContract.View<Presenter> {

    }

    interface Presenter extends BaseContract.Presenter {
        List<WalletData> fetchWallet();
        List<CoinData> fetchSupportCoin();
    }
}
