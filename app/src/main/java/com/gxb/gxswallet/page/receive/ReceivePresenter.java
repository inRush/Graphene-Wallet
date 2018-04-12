package com.gxb.gxswallet.page.receive;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.coin.CoinData;
import com.gxb.gxswallet.db.coin.CoinDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.sxds.common.presenter.BasePresenter;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/17.
 */

public class ReceivePresenter extends BasePresenter<ReceiveContract.View>
        implements ReceiveContract.Presenter {
    ReceivePresenter(ReceiveContract.View view) {
        super(view);
        mWalletDataManager = new WalletDataManager(App.getInstance());
        mCoinDataManager = new CoinDataManager(App.getInstance());
    }

    private WalletDataManager mWalletDataManager;
    private CoinDataManager mCoinDataManager;


    @Override
    public List<CoinData> fetchSupportCoin() {
        return mCoinDataManager.queryAll();
    }

    @Override
    public List<WalletData> fetchWallet() {
        return mWalletDataManager.queryAll();
    }
}
