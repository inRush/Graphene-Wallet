package com.gxb.gxswallet.page.importaccount;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.services.WalletService;
import com.sxds.common.presenter.BasePresenter;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/13.
 */

public class ImportWalletPresenter extends BasePresenter<ImportWalletContract.View>
        implements ImportWalletContract.Presenter {

    private WalletDataManager mWalletDataManager;

    ImportWalletPresenter(ImportWalletContract.View view) {
        super(view);
        mWalletDataManager = new WalletDataManager(App.getInstance());
    }

    @Override
    public void importAccount(String wifKey, String password) {
        try {
            WalletService.getInstance()
                    .importAccount(wifKey, password, new WalletService.ServerListener<List<WalletData>>() {
                        @Override
                        public void onFailure(Error error) {
                            getView().showError(error.getMessage());
                        }

                        @Override
                        public void onSuccess(List<WalletData> data) {
                            mWalletDataManager.insertMult(data);
                            getView().onImportAccountSuccess(data);
                        }
                    });
        } catch (IllegalArgumentException e) {
            getView().showError(App.getInstance().getString(R.string.activity_private_key_format_error));
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void destroy() {
    }
}
