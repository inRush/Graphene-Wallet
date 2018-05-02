package com.gxb.gxswallet.page.send;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.google.common.base.Function;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.dialog.PasswordDialog;
import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.config.Configure;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.send.dialog.TransactionConfirmDialog;
import com.gxb.gxswallet.page.send.model.Sender;
import com.gxb.gxswallet.page.send.model.Transaction;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.gxswallet.utils.AssetsUtil;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.jwsd.libzxing.OnQRCodeScanCallback;
import com.jwsd.libzxing.QRCodeManager;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sxds.common.app.PresenterActivity;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/16.
 */

public class SendActivity extends PresenterActivity<SendContract.Presenter>
        implements SendContract.View, PasswordDialog.OnPasswordConfirmListener {

    @BindView(R.id.topbar_send)
    QMUITopBar mTopBar;
    @BindView(R.id.from_send)
    TextView fromTv;
    @BindView(R.id.to_send)
    EditText toEt;
    @BindView(R.id.amount_send)
    EditText amountEt;
    @BindView(R.id.memo_send)
    EditText memoEt;
    @BindView(R.id.available_gxs_send)
    TextView gxsTv;
    @BindView(R.id.avatar_from_send)
    ImageView fromAvatarIv;
    @BindView(R.id.avatar_to_from_send)
    ImageView toAvatarIv;
    @BindView(R.id.asset_send)
    TextView assetTv;
    @BindView(R.id.image_asset_send)
    ImageView assetImageIv;

    private Sender mSender;
    private WalletData mCurrentWallet;
    private String[] mWalletNames;
    private String[] mAssetNames;
    private static final String SENDER_KEY = "sender";

    public static void start(Activity activity, Sender sender) {
        Intent intent = new Intent(activity, SendActivity.class);
        intent.putExtra(SENDER_KEY, sender);
        activity.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mSender = bundle.getParcelable(SENDER_KEY);
            mCurrentWallet = WalletManager.getInstance().getCurrentWallet();
            return super.initArgs(bundle);
        }
        return false;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTopBar.setTitle(getString(R.string.send));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        if (mCurrentWallet == null) {
            mCurrentWallet = WalletManager.getInstance().getCurrentWallet();
        }
        initFromAccountWidget();
        initAssetWidget();
        initToAccountWidget();
        memoEt.setText(mSender.getMemo());
        QMUIAlphaImageButton imageButton = mTopBar.addRightImageButton(R.drawable.ic_scan, View.generateViewId());
        imageButton.setOnClickListener(v -> onScanQR());
    }


    @Override
    protected void initData() {
        super.initData();
        List<WalletData> walletDataList = mPresenter.fetchWallet();
        mWalletNames = new String[walletDataList.size()];
        for (int i = 0; i < walletDataList.size(); i++) {
            mWalletNames[i] = walletDataList.get(i).getName();
        }
        List<AssetData> assetDataList = mPresenter.fetchAssets();
        mAssetNames = new String[assetDataList.size()];
        for (int i = 0; i < assetDataList.size(); i++) {
            mAssetNames[i] = assetDataList.get(i).getName();
        }
    }

    public void onScanQR() {
        QRCodeManager.getInstance()
                .with(this)
                .scanningQRCode(new OnQRCodeScanCallback() {
                    @Override
                    public void onCompleted(String result) {
                        if (result.indexOf(Configure.QR_CODE_PRE_FIX) == 0) {
                            try {
                                result = result.replace(Configure.QR_CODE_PRE_FIX, "");
                                String[] res = result.split("&");
                                String account = res[0].split("=")[1];
                                String amount = res[1].split("=")[1];
                                String coin = res[2].split("=")[1];
                                if ("".equals(amount)) {
                                    amount = "0";
                                }
                                Double.parseDouble(amount);
                                mSender.setTo(account);
                                mSender.setAmount(amount);
                                mSender.setAsset(AssetDataManager.get(coin));
                                initToAccountWidget();
                                initAssetWidget();
                                App.showToast(R.string.scan_success);
                            } catch (Exception e) {
                                App.showToast(R.string.scan_error);
                            }
                        } else {
                            App.showToast(R.string.scan_gxb_qrcode);
                        }
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        App.showToast(R.string.scan_error);
                    }

                    @Override
                    public void onCancel() {
                        //取消扫描的时候回调
                    }
                });
    }

    private void initFromAccountWidget() {
        try {
            fromAvatarIv.setImageDrawable(Jdenticon.from(mCurrentWallet.getName()).drawable());
            fromTv.setText(mCurrentWallet.getName());
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    private void initAssetWidget() {
        amountEt.setHint(getString(R.string.amount_to_send, mSender.getAsset().getName()));
        assetTv.setText(mSender.getAsset().getName());
        assetImageIv.setImageBitmap(AssetsUtil.getImage(mSender.getAsset().getIcon()));
        amountEt.setText(mSender.getAmount());
        mPresenter.fetchWalletBalance(mCurrentWallet, mSender.getAsset());
    }

    /**
     * 显示菜单
     *
     * @param items
     * @param checkedIndex
     */
    private void showChooseAccountDialog(String[] items, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    mCurrentWallet = mPresenter.fetchWallet().get(which);
                    initFromAccountWidget();
                    initAssetWidget();
                })
                .show();
    }

    private void showChooseAssetDialog(String[] items, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    mSender.setAsset(AssetDataManager.get(mAssetNames[which]));
                    initAssetWidget();
                })
                .show();
    }

    private void initToAccountWidget() {
        try {
            String name = "";
            if (mSender.getTo() != null) {
                name = mSender.getTo();
            }
            toAvatarIv.setImageDrawable(Jdenticon.from(name).drawable());
            toEt.setText(mSender.getTo());
            toEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        toAvatarIv.setImageDrawable(Jdenticon.from(toEt.getText().toString()).drawable());
                    } catch (IOException | SVGParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.send_btn_send)
    void onSendBtnClick() {
        if ("".equals(toEt.getText().toString())) {
            showError(getString(R.string.to_account_not_allow_empty));
            return;
        }
        if ("".equals(amountEt.getText().toString()) || "0".equals(amountEt.getText().toString())) {
            showError(getString(R.string.amount_format_error));
            return;
        }
        new PasswordDialog().setPasswordConfirmListener(this).show(getSupportFragmentManager(), "password");
    }

    @OnClick(R.id.switch_account_send)
    void onSwitchAccountBtnClick() {
        showChooseAccountDialog(mWalletNames, getCurrentWalletIndex());
    }


    @OnClick(R.id.switch_asset_send)
    void onSwitchAssetBtnClick() {
        showChooseAssetDialog(mAssetNames, getCurrentAssetIndex());
    }

    /**
     * 获取当前钱包的索引
     *
     * @return 当前钱包的索引
     */
    private int getCurrentWalletIndex() {
        for (int i = 0; i < mWalletNames.length; i++) {
            if (mCurrentWallet.getName().equals(mWalletNames[i])) {
                return i;
            }
        }
        return 0;
    }

    private int getCurrentAssetIndex() {
        for (int i = 0; i < mAssetNames.length; i++) {
            if (mSender.getAsset().getName().equals(mAssetNames[i])) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onSendSuccess() {
        dismissAllLoading();
        showOk(getString(R.string.send_success));
    }

    @Override
    public void onQueryFeeSuccess(double fee, Function<Boolean, Void> callback) {
        dismissAllLoading();
        TransactionConfirmDialog.newInstance(
                new Transaction(Double.parseDouble(amountEt.getText().toString()),
                        toEt.getText().toString(),
                        memoEt.getText().toString(),
                        fee, mSender.getAsset().getName()))
                .setOnTransactionConfirmListener(new TransactionConfirmDialog.OnTransactionConfirmListener() {
                    @Override
                    public void onConfirm() {
                        callback.apply(true);
                    }

                    @Override
                    public void onCancel() {
                        callback.apply(false);
                    }
                })
                .show(getSupportFragmentManager(), "confirm");
    }

    @Override
    public void onFetchWalletBalanceSuccess(WalletData wallet, double balance) {
        wallet.setBalances(mSender.getAsset().getName(), balance);
        double amount = mCurrentWallet.getBalances(mSender.getAsset().getName());
        gxsTv.setText(getString(R.string.available_coin, String.valueOf(amount), mSender.getAsset().getName()));
    }

    @Override
    protected SendContract.Presenter initPresenter() {
        return new SendPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_send;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册onActivityResult
        QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfirm(String password) {
        String wifKey = WalletService.getInstance().unlockWallet(mCurrentWallet, password)[0];
        if (wifKey == null) {
            showError(getString(R.string.password_error));
            return;
        }
        ECKey privateKey = DumpedPrivateKey.fromBase58(null, wifKey).getKey();
        String from = fromTv.getText().toString();
        String to = toEt.getText().toString();
        String amount = amountEt.getText().toString();
        String memo = memoEt.getText().toString();
        try {
            int num = (int) (Double.parseDouble(amount) * AssetDataManager.AMOUNT_SIZE);
            mPresenter.send(privateKey, from, to, String.valueOf(num), mSender.getAsset(), memo);
        } catch (Exception e) {
            showError(R.string.amount_format_error);
        }

    }

}
