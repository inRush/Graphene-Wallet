package com.gxb.gxswallet.page.walletdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.page.backuptip.BackUpTipActivity;
import com.gxb.gxswallet.page.walletdetail.dialogbuilder.ModifyPasswordDialogBuilder;
import com.gxb.gxswallet.page.walletdetail.dialogbuilder.PrivateKeyDialogBuilder;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sxds.common.app.BaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/4/1.
 */

public class WalletDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.topbar_wallet_detail)
    QMUITopBar mTopBar;
    @BindView(R.id.avatar_wallet_detail)
    ImageView mAvatarIv;
    @BindView(R.id.wallet_name_wallet_detail)
    TextView mNameTv;
    @BindView(R.id.groupListView_wallet_detail)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.backup_words_wallet_detail)
    QMUIRoundButton mBackupWordBtn;

    private static final String DATA_KEY = "wallet";
    private WalletDataManager mWalletDataManager;
    private WalletData mWalletData;
    private String[] partOneSettings;
    private String[] partTwoSettings;

    public static void start(Activity activity, WalletData wallet) {
        Intent intent = new Intent(activity, WalletDetailActivity.class);
        intent.putExtra(DATA_KEY, wallet);
        activity.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mWalletData = bundle.getParcelable(DATA_KEY);
        }
        return super.initArgs(bundle);
    }

    @Override
    protected void initData() {
        super.initData();
        mWalletDataManager = new WalletDataManager(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        try {
            initTopBar();
            initMenus();
            if (mWalletData.getIsBackUp()) {
                mBackupWordBtn.setVisibility(View.GONE);
            }
            mAvatarIv.setImageDrawable(Jdenticon.from(mWalletData.getName()).drawable());
            mNameTv.setText(mWalletData.getName());
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }


    private void initMenus() {
        partOneSettings = new String[]{
                getString(R.string.wallet_name),
                getString(R.string.wallet_assets, AssetDataManager.getDefault().getName()),
                getString(R.string.modify_password),
        };
        partTwoSettings = new String[]{
                getString(R.string.export_private_key)
        };

        QMUICommonListItemView walletNameItem = mGroupListView.createItemView(partOneSettings[0]);
        walletNameItem.setOrientation(QMUICommonListItemView.VERTICAL);
        walletNameItem.setDetailText(mWalletData.getName());

        QMUICommonListItemView walletAssetsItem = mGroupListView.createItemView(partOneSettings[1]);
        if (mWalletData.getBalances(AssetDataManager.getDefault().getName()) != null) {
            walletAssetsItem.setDetailText(String.valueOf(
                    mWalletData.getBalances(AssetDataManager.getDefault().getName()))
            );
        }

        QMUICommonListItemView modifyPasswordItem = mGroupListView.createItemView(partOneSettings[2]);
        modifyPasswordItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(this)
                .setTitle("")
                .addItemView(walletNameItem, this)
                .addItemView(walletAssetsItem, this)
                .addItemView(modifyPasswordItem, this)
                .addTo(mGroupListView);


        QMUICommonListItemView exportPrivateKey = mGroupListView.createItemView(partTwoSettings[0]);
        exportPrivateKey.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUIGroupListView.newSection(this)
                .setTitle("")
                .addItemView(exportPrivateKey, this)
                .addTo(mGroupListView);

    }


    private void initTopBar() {
        mTopBar.setTitle(getString(R.string.wallet_detail));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    @OnClick(R.id.backup_words_wallet_detail)
    void onBackUpWordsBtnClick() {
        BackUpTipActivity.start(this, mWalletData, false);
        finish();
    }

    @OnClick(R.id.delete_wallet_wallet_detail)
    void onDeleteWalletBtnClick() {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.confirm_delete_important))
                .addAction(getString(R.string.cancel), (dialog, index) -> dialog.dismiss())
                .addAction(0, getString(R.string.delete), QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                    dialog.dismiss();
                    if (WalletManager.getInstance().deleteWallet(mWalletData)) {
                        App.showToast(R.string.delete_success);
                        finish();
                    } else {
                        showError(getString(R.string.delete_failure));
                    }
                })
                .show();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof QMUICommonListItemView) {
            String text = ((QMUICommonListItemView) view).getText().toString();
            if (partTwoSettings[0].equals(text)) {
                showExportPrivateKeyDialog();
            } else if (partOneSettings[2].equals(text)) {
                showModifyPasswordDialog();
            }
        }
    }

    private void showExportPrivateKeyDialog() {
        PrivateKeyDialogBuilder dialog = new PrivateKeyDialogBuilder(this, mWalletData);
        dialog.show();
        QMUIKeyboardHelper.showKeyboard(dialog.getEditText(), true);
    }

    private void showModifyPasswordDialog() {
        ModifyPasswordDialogBuilder dialog = new ModifyPasswordDialogBuilder(this, mWalletData);
        dialog.show();
        QMUIKeyboardHelper.showKeyboard(dialog.getEditText(), true);
    }
}
