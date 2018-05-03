package com.gxb.gxswallet.page.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.main.MainActivity;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sxds.common.app.BaseActivity;
import com.sxds.common.utils.LanguageUtil;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/4/12.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.topbar_setting)
    QMUITopBar mTopBar;
    @BindView(R.id.groupListView_setting)
    QMUIGroupListView mGroupListView;

    private String[] languages;
    private String[] walletNames;
    private String[] assetNames;
    private String language;

    private QMUICommonListItemView defaultWalletItem;
    private QMUICommonListItemView defaultAssetItem;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        QMUICommonListItemView languageItem = mGroupListView.createItemView(getString(R.string.language));
        initLanguage();
        language = LanguageUtil.getInstance().getAppLanguage(this);
        if (language.equals(Locale.CHINA.getLanguage())) {
            language = getString(R.string.chinese);
        } else if (language.equals(Locale.ENGLISH.getLanguage())) {
            language = getString(R.string.english);
        }
        languageItem.setDetailText(language);
        QMUIGroupListView.newSection(this)
                .setTitle("")
                .addItemView(languageItem, this)
                .addTo(mGroupListView);

        initWallets();
        initAssetNames();
        defaultWalletItem = mGroupListView.createItemView(getString(R.string.default_wallet));
        defaultWalletItem.setDetailText(WalletManager.getInstance().getDefaultWallet().getName());
        defaultAssetItem = mGroupListView.createItemView(getString(R.string.default_asset));
        defaultAssetItem.setDetailText(AssetDataManager.getDefault().getName());
        QMUIGroupListView.newSection(this)
                .setTitle("")
                .addItemView(defaultWalletItem, this)
                .addItemView(defaultAssetItem, this)
                .addTo(mGroupListView);
    }

    private void initLanguage() {
        languages = new String[]{
                getString(R.string.chinese),
                getString(R.string.english)
        };
    }

    private void initWallets() {
        List<WalletData> walletDataList = WalletManager.getInstance().getAllWallet();
        walletNames = new String[walletDataList.size()];
        int index = 0;
        for (WalletData wallet : walletDataList) {
            walletNames[index++] = wallet.getName();
        }
    }

    private void initAssetNames() {
        List<AssetData> assetDataList = AssetDataManager.getEnableList();
        assetNames = new String[assetDataList.size()];
        int index = 0;
        for (AssetData asset : assetDataList) {
            assetNames[index++] = asset.getName();
        }
    }

    private void initTopBar() {
        mTopBar.setTitle(R.string.system_setting);
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View view) {
        if (view instanceof QMUICommonListItemView) {
            String text = ((QMUICommonListItemView) view).getText().toString();
            if (getString(R.string.language).equals(text)) {
                showLanguageDialog(languages, getCurrentLanguageIndex());
            } else if (getString(R.string.default_wallet).equals(text)) {
                showWalletDialog(walletNames, getCurrentDefaultWalletIndex());
            } else if (getString(R.string.default_asset).equals(text)) {
                showAssetDialog(assetNames, getDefaultAssetIndex());
            }
        }
    }

    private void showAssetDialog(String[] items, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    AssetData assetData = AssetDataManager.get(assetNames[which]);
                    AssetDataManager.setDefault(assetData);
                    defaultAssetItem.setDetailText(assetData.getName());
                })
                .show();
    }

    private void showLanguageDialog(String[] items, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    Locale locale = Locale.SIMPLIFIED_CHINESE;
                    if (which == 1) {
                        locale = Locale.ENGLISH;
                    }
                    LanguageUtil.getInstance().changeAppLanguageSetting(SettingActivity.this, MainActivity.class, locale);
                })
                .show();
    }

    private void showWalletDialog(String[] items, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    WalletData wallet = WalletManager.getInstance().getWallet(walletNames[which]);
                    WalletManager.getInstance().setDefaultWallet(wallet);
                    defaultWalletItem.setDetailText(wallet.getName());
                })
                .show();
    }

    private int getDefaultAssetIndex() {
        AssetData assetData = AssetDataManager.getDefault();
        for (int i = 0; i < assetNames.length; i++) {
            if (assetData.getName().equals(assetNames[i])) {
                return i;
            }
        }
        return 0;
    }

    private int getCurrentLanguageIndex() {
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(language)) {
                return i;
            }
        }
        return 0;
    }

    private int getCurrentDefaultWalletIndex() {
        WalletData defaultWallet = WalletManager.getInstance().getDefaultWallet();
        for (int i = 0; i < walletNames.length; i++) {
            if (defaultWallet.getName().equals(walletNames[i])) {
                return i;
            }
        }
        return 0;
    }
}
