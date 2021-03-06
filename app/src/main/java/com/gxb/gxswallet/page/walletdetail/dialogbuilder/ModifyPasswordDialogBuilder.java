package com.gxb.gxswallet.page.walletdetail.dialogbuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.manager.WalletManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.services.WalletService;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import net.qiujuer.genius.ui.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/5/2.
 */

public class ModifyPasswordDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {

    @BindView(R.id.password_dialog_modify_password)
    EditText mPassword;
    @BindView(R.id.again_password_dialog_modify_password)
    EditText mAgainPassword;
    @BindView(R.id.old_password_dialog_modify_password)
    EditText mOldPassword;

    QMUIDialog mDialog;
    private WalletData mWalletData;


    public ModifyPasswordDialogBuilder(Context context, WalletData walletData) {
        super(context);
        this.mWalletData = walletData;
    }

    @Override
    public QMUIDialog show() {
        mDialog = super.show();
        return mDialog;
    }

    public android.widget.EditText getEditText() {
        return mOldPassword;
    }

    @Override
    public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_modify_password, parent, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btn_dialog_modify_password)
    void onOkBtnClick() {
        String password = mPassword.getText().toString();
        String againPassword = mAgainPassword.getText().toString();
        String oldPassword = mOldPassword.getText().toString();
        if ("".equals(password) || "".equals(againPassword) || "".equals(oldPassword)) {
            App.showToast(R.string.password_not_allow_empty);
            return;
        }
        if (!password.equals(againPassword)) {
            App.showToast(R.string.password_not_match);
            return;
        }
        String wifKey = WalletService.getInstance().unlockWallet(mWalletData, oldPassword)[0];

        if (wifKey == null) {
            App.showToast(R.string.password_error);
            return;
        }
        WalletService.getInstance().lockWallet(mWalletData, wifKey, password, oldPassword);
        if (WalletManager.getInstance().updateWallet(mWalletData)) {
            App.showToast(R.string.modify_success);
        } else {
            App.showToast(R.string.modify_failure);
        }
        mDialog.dismiss();
    }
}
