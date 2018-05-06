package com.gxb.gxswallet.page.createwallet;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.services.WalletService;
import com.sxds.common.presenter.BasePresenter;

import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author inrush
 * @date 2018/3/31.
 */

class CreateWalletPresenter extends BasePresenter<CreateWalletContract.View>
        implements CreateWalletContract.Presenter {

    private static final int CHECK_EXIST_LOADING_CODE = 0x100;
    private static final int CREATE_WALLET_LOADING_CODE = 0x100;
    private static final int WALLET_NAME_MINIMUM_LENGTH = 3;

    private enum WalletNameError {
        START_REQUEST_LETTER(Pattern.compile("^[a-z]"), App.getInstance().getString(R.string.start_request_letter)),
        ONLY_LETTER_NUMBER_LINE(Pattern.compile("^[a-z0-9-]*$"), App.getInstance().getString(R.string.only_letter_number_line)),
        ONLY_ONE_DASH(Pattern.compile("--"), App.getInstance().getString(R.string.only_one_dash)),
        END_REQUEST_NUMBER_OR_LETTER(Pattern.compile("[a-z0-9]$"), App.getInstance().getString(R.string.end_request_number_or_letter)),
        LENGTH_TOO_SHORT(null, App.getInstance().getString(R.string.wallet_name_request_more_than_three));

        private final String error;
        private final Pattern pattern;

        WalletNameError(Pattern pattern, String errorStr) {
            this.error = errorStr;
            this.pattern = pattern;
        }

        private boolean match(String str) {
            return this.pattern.matcher(str).find();
        }
    }

    CreateWalletPresenter(CreateWalletContract.View view) {
        super(view);
    }


    @Override
    public void checkWalletExist(String name) {
        getView().showLoading(CHECK_EXIST_LOADING_CODE, App.getInstance().getString(R.string.check_wallet_name));
        WalletService.getInstance().fetchAllAccountByName(name)
                .map(accountMap -> accountMap.size() > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hasAccount -> getView().onCheckWalletExistSuccess(hasAccount, name), error -> {
                    getView().dismissLoading(CHECK_EXIST_LOADING_CODE);
                    getView().onCheckWalletExistError(new Error(error.getMessage()));
                });
    }


    @Override
    public String checkWalletName(String name) {
        if (!WalletNameError.START_REQUEST_LETTER.match(name)) {
            return WalletNameError.START_REQUEST_LETTER.error;
        }
        if (!WalletNameError.ONLY_LETTER_NUMBER_LINE.match(name)) {
            return WalletNameError.ONLY_LETTER_NUMBER_LINE.error;
        }
        if (WalletNameError.ONLY_ONE_DASH.match(name)) {
            return WalletNameError.ONLY_ONE_DASH.error;
        }
        if (!WalletNameError.END_REQUEST_NUMBER_OR_LETTER.match(name)) {
            return WalletNameError.END_REQUEST_NUMBER_OR_LETTER.error;
        }
        if (name.length() < WALLET_NAME_MINIMUM_LENGTH) {
            return WalletNameError.LENGTH_TOO_SHORT.error;
        }
        return null;
    }

    @Override
    public void createWallet(String walletName, String password) {
        getView().showLoading(CREATE_WALLET_LOADING_CODE, R.string.being_created_wallet);
        WalletService.getInstance()
                .createAccount(walletName, password, new WalletService.ServerListener<WalletData>() {
                    @Override
                    public void onFailure(Error error) {
                        getView().dismissLoading(CREATE_WALLET_LOADING_CODE);
                        getView().showError(error.getMessage());
                    }

                    @Override
                    public void onSuccess(WalletData data) {
                        getView().dismissLoading(CREATE_WALLET_LOADING_CODE);
                        getView().onWalletCreateSuccess(data);
                    }
                });
    }
}
