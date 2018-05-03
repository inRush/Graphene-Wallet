package com.gxb.gxswallet.page.walletdetail.dialogbuilder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.services.WalletService;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import net.qiujuer.genius.ui.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * @author inrush
 * @date 2018/5/2.
 */


public class PrivateKeyDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {

    @BindView(R.id.placeholder_dialog_private_key)
    TextView mPlacegholder;
    @BindView(R.id.private_key_dialog_private_key)
    TextView mPrivateKey;
    @BindView(R.id.password_dialog_private_key)
    EditText mPasswordEt;

    private ClipboardManager mClipboard;
    private WalletData mWalletData;


    public PrivateKeyDialogBuilder(Context context, WalletData walletData) {
        super(context);
        mClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        this.mWalletData = walletData;
    }

    public android.widget.EditText getEditText() {
        return mPasswordEt;
    }


    @Override
    public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_show_private_key, parent, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btn_dialog_private_key)
    void onBtnClick(View v) {
        QMUIRoundButton button = (QMUIRoundButton) v;
        if (button.getText().toString().equals(mContext.getString(R.string.unlock_wallet))) {
            String password = mPasswordEt.getText().toString();
            String[] keys = WalletService.getInstance().unlockWallet(mWalletData, password);
            if (keys[0] == null) {
                App.showToast(mContext.getString(R.string.password_error));
            } else {
                mPlacegholder.setVisibility(View.GONE);
                mPrivateKey.setText(keys[0]);
                button.setText(mContext.getString(R.string.copy));
            }
        } else {
            ClipData clipData = ClipData.newPlainText("PrivateKey", mPrivateKey.getText().toString());
            mClipboard.setPrimaryClip(clipData);
            App.showToast(R.string.copy_success);
        }
    }
}
