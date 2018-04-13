package com.gxb.gxswallet.page.createwallet;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.ImageView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.backuptip.BackUpTipActivity;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sxds.common.app.PresenterActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/31.
 */

public class CreateWalletActivity extends PresenterActivity<CreateWalletContract.Presenter>
        implements CreateWalletContract.View {

    @BindView(R.id.topbar_create_wallet)
    QMUITopBar mTopBar;
    @BindView(R.id.wallet_name_create_wallet)
    EditText mWalletNameEt;
    @BindView(R.id.avatar_create_wallet)
    ImageView mAvatarIv;
    @BindView(R.id.password_create_wallet)
    EditText mPasswordEt;
    @BindView(R.id.again_password_create_wallet)
    EditText mAgainPasswordEt;

    private SparseArray<QMUITipDialog> mLoadingMap = new SparseArray<>();
    private static final int PASSWORD_MINIMUM_LENGTH = 6;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CreateWalletActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        initWalletNameEt();
    }

    private void initTopBar() {
        mTopBar.setTitle(getString(R.string.create_wallet));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    private void initWalletNameEt() {
        try {
            mAvatarIv.setImageDrawable(Jdenticon.from("").drawable());
            mWalletNameEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        mAvatarIv.setImageDrawable(Jdenticon.from(mWalletNameEt.getText().toString()).drawable());
                    } catch (IOException | SVGParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.create_btn_create_wallet)
    void onCreateBtnClick() {
        String walletName = mWalletNameEt.getText().toString();
        if ("".equals(walletName)) {
            showError(getString(R.string.wallet_name_not_allow_empty));
            return;
        }
        String error = mPresenter.checkWalletName(walletName);
        if (error != null) {
            showError(error);
            return;
        }
        String password = mPasswordEt.getText().toString();
        if ("".equals(password)) {
            showError(getString(R.string.password_not_allow_empty));
            return;
        }
        if (password.length() < PASSWORD_MINIMUM_LENGTH) {
            showError(getString(R.string.password_request_more_than_six));
            return;
        }
        String againPassword = mAgainPasswordEt.getText().toString();
        if (!againPassword.equals(password)) {
            showError(getString(R.string.password_not_match));
            return;
        }
        mPresenter.checkWalletExist(mWalletNameEt.getText().toString());
    }

    @Override
    protected CreateWalletContract.Presenter initPresenter() {
        return new CreateWalletPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_create_wallet;
    }

    @Override
    public void onCheckWalletExistSuccess(boolean isExist, String walletName) {
        if (isExist) {
            showError(getString(R.string.this_wallet_name_already_exist));
        } else {
            String password = mPasswordEt.getText().toString();
            mPresenter.createWallet(walletName, password);
        }
    }

    @Override
    public void onCheckWalletExistError(Error error) {
        showError(error.getMessage());
    }

    @Override
    public void onWalletCreateSuccess(WalletData wallet) {
        finish();
        BackUpTipActivity.start(this, wallet,true);
        App.showToast(R.string.create_wallet_success);
    }
}
