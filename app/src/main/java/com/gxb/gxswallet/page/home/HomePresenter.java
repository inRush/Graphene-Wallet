package com.gxb.gxswallet.page.home;

import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;

import com.gxb.gxswallet.db.coin.CoinData;
import com.gxb.gxswallet.db.coin.CoinDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.gxswallet.utils.AssetsUtil;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.sxds.common.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inrush
 * @date 2018/3/14.
 */

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private WalletDataManager mWalletDataManager;
    private CoinDataManager mCoinDataManager;
    private ServiceConnection mConnection;
    private Context mContext;
    HomePresenter(HomeContract.View view,Context context) {
        super(view);
        mWalletDataManager = new WalletDataManager(context);
        mCoinDataManager = new CoinDataManager(context);
        mContext = context;
    }


    @Override
    public void fetchWalletBalance(final WalletData wallet) {
        WalletService.getInstance()
                .fetchAccountBalance(wallet.getName(),
                        new WalletService.ServerListener<List<AccountBalance>>() {
                            @Override
                            public void onFailure(Error error) {

                            }

                            @Override
                            public void onSuccess(final List<AccountBalance> data) {
                                Run.onUiAsync(() -> getView().onFetchWalletBalanceSuccess(wallet, data));
                            }
                        });
    }

    @Override
    public List<CoinData> fetchSupportCoin() {
        return mCoinDataManager.queryEnableCoin();
    }

    @Override
    public List<CoinItem> convertToCoinItem(List<CoinData> coinDatas) {
        List<CoinItem> coinItems = new ArrayList<>();
        for (CoinData coinData : coinDatas) {
            Bitmap icon = AssetsUtil.getImage(coinData.getIcon());
            coinItems.add(new CoinItem(icon, coinData.getName(), 0, coinData.getAssetId(), 0, coinData.getEnable()));
        }
        return coinItems;
    }

    @Override
    public List<WalletData> fetchWallet() {
        return mWalletDataManager.queryAll();
    }
}
