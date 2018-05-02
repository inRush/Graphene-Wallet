package com.gxb.gxswallet.page.walletmanager;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.sxds.common.presenter.BasePresenter;

import java.util.List;

import cy.agorise.graphenej.api.android.WebSocketService;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author inrush
 * @date 2018/3/27.
 */

public class WalletManagerPresenter extends BasePresenter<WalletManagerContract.View>
        implements WalletManagerContract.Presenter {

    private WalletDataManager mWalletDataManager;

    public WalletManagerPresenter(WalletManagerContract.View view) {
        super(view);
        mWalletDataManager = new WalletDataManager(App.getInstance());

    }

    @Override
    public List<WalletData> fetchWallets() {
        return mWalletDataManager.queryAll();
    }

    @Override
    public void fetchWalletBalance(final WalletData wallet, AssetData asset) {
        WebSocketService service = WebSocketServicePool.getInstance().getService(asset.getName());
        WalletService.getInstance().fetchAccountBalance(service, wallet.getName(), asset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> getView().onFetchWalletBalanceSuccess(wallet, result),
                        error -> getView().showError(error.getMessage()));
    }

}
