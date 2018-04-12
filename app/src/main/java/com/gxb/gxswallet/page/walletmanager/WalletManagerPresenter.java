package com.gxb.gxswallet.page.walletmanager;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.sxds.common.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

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
    public void fetchWalletBalance(final WalletData wallet) {
        WalletService.getInstance()
                .fetchAccountBalance(wallet.getName(),
                        new WalletService.ServerListener<List<AccountBalance>>() {
                            @Override
                            public void onFailure(Error error) {

                            }

                            @Override
                            public void onSuccess(final List<AccountBalance> data) {
                                Run.onUiAsync(new Action() {
                                    @Override
                                    public void call() {
                                        getView().onFetchWalletBalanceSuccess(wallet, data);
                                    }
                                });
                            }
                        });
    }

}
