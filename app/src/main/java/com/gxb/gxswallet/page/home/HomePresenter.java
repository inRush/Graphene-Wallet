package com.gxb.gxswallet.page.home;

import android.content.Context;
import android.graphics.Bitmap;

import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.gxswallet.utils.AssetsUtil;
import com.sxds.common.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inrush
 * @date 2018/3/14.
 */

public class HomePresenter extends BasePresenter<HomeContract.View>
        implements HomeContract.Presenter {

    private AssetDataManager mCoinDataManager;

    HomePresenter(HomeContract.View view, Context context) {
        super(view);
        mCoinDataManager = new AssetDataManager(context);
    }


    @Override
    public void fetchWalletBalance(final WalletData wallet) {
        WalletService.getInstance().fetchAllAccountBalance(wallet.getName())
                .subscribe(result -> getView().onFetchWalletBalanceSuccess(wallet, result),
                        error -> getView().showError(error.getMessage()));

    }

    @Override
    public List<AssetData> fetchSupportCoin() {
        return mCoinDataManager.queryEnableAsset();
    }

    @Override
    public List<CoinItem> convertToCoinItem(List<AssetData> coinDatas) {
        List<CoinItem> coinItems = new ArrayList<>();
        for (AssetData coinData : coinDatas) {
            Bitmap icon = AssetsUtil.getImage(coinData.getIcon());
            coinItems.add(new CoinItem(icon, coinData.getName(), 0, coinData.getAssetId(), 0, coinData.getEnable()));
        }
        return coinItems;
    }

}
