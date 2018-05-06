package com.gxb.gxswallet.page.backuptip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.dialog.PasswordDialog;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.manager.WalletManager;
import com.gxb.gxswallet.page.main.MainActivity;
import com.gxb.gxswallet.services.WalletService;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sxds.common.app.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/4/1.
 */

public class BackUpTipActivity extends BaseActivity implements PasswordDialog.OnPasswordConfirmListener {

    @BindView(R.id.topbar_backup_tip)
    QMUITopBar mTopBar;
    @BindView(R.id.memorizing_words_backup_tip)
    TextView mMemorizingWordTv;
    @BindView(R.id.unlock_wallet_hint_backup_tip)
    TextView mUnlockWalletHintTV;


    private static final String DATA_KEY = "wallet";
    private static final String START_KEY = "main";
    private WalletData mWalletData;
    private boolean isStartMain;


    public static void start(Activity activity, WalletData wallet, boolean startMain) {
        Intent intent = new Intent(activity, BackUpTipActivity.class);
        intent.putExtra(DATA_KEY, wallet);
        intent.putExtra(START_KEY, startMain);
        activity.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mWalletData = bundle.getParcelable(DATA_KEY);
            isStartMain = bundle.getBoolean(START_KEY);
            return super.initArgs(bundle);
        }
        return false;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTopBar.setTitle(getString(R.string.backup_wallet));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> showConfirm());
    }

    private void showConfirm() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(getString(R.string.tip))
                .setMessage(getString(R.string.confirm_backup_has_been_completed))
                .addAction(getString(R.string.cancel), (dialog, index) -> dialog.dismiss())
                .addAction(getString(R.string.ok), (dialog, index) -> {
                    dialog.dismiss();
                    mWalletData.setBrainKey("");
                    mWalletData.setIsBackUp(true);
                    WalletManager.getInstance().updateWallet(mWalletData);
                    finish();
                    if (isStartMain) {
                        MainActivity.start(this);
                    }
                })
                .addAction(getString(R.string.next_time_say), (dialog, index) -> {
                    dialog.dismiss();
                    finish();
                    if (isStartMain) {
                        MainActivity.start(this);
                    }
                })
                .show();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_backup_tip;
    }

    @OnClick(R.id.backup_wallet_btn_backup_tip)
    void onBackupWalletBtnClick() {
        new PasswordDialog().setPasswordConfirmListener(this).show(getSupportFragmentManager(), "password");
    }

    @Override
    public void onConfirm(String password) {
        String[] keys = WalletService.getInstance()
                .unlockWallet(mWalletData, password);
        if (keys[0] == null) {
            showError(getString(R.string.password_error));
        } else {
            mUnlockWalletHintTV.setVisibility(View.GONE);
            if (keys[1] != null) {
                mMemorizingWordTv.setText(keys[1]);
            } else {
                showError(getString(R.string.wallet_already_backup));
            }
        }
    }
}
