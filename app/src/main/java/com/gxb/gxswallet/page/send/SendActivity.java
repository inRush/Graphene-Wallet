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
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.config.AssetSymbol;
import com.gxb.gxswallet.config.Configure;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.send.model.Sender;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.jwsd.libzxing.OnQRCodeScanCallback;
import com.jwsd.libzxing.QRCodeManager;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sxds.common.app.PresenterActivity;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/16.
 */

public class SendActivity extends PresenterActivity<SendContract.Presenter>
        implements SendContract.View {

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

    private Sender mSender;
    private WalletData mCurrentWallet;
    private List<WalletData> mWalletDataList;
    private String[] mWalletNames;
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
            mCurrentWallet = mSender.getFrom();
            return super.initArgs(bundle);
        }
        return false;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTopBar.setTitle(getString(R.string.send));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        initToAccountWidget();
        QMUIAlphaImageButton imageButton = mTopBar.addRightImageButton(R.drawable.ic_scan, View.generateViewId());
        imageButton.setOnClickListener(v -> onScanQR());
    }

    @Override
    protected void initData() {
        super.initData();
        mWalletDataList = mPresenter.fetchWallet();
        mWalletNames = new String[mWalletDataList.size()];
        for (int i = 0; i < mWalletDataList.size(); i++) {
            mWalletNames[i] = mWalletDataList.get(i).getName();
        }
        mPresenter.fetchWalletBalance(mCurrentWallet);
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
                                toEt.setText(account);
                                amountEt.setText(amount);
                                double avaAmount = mCurrentWallet.getBalances(coin).getAmount() / 100000.0;
                                gxsTv.setText(getString(R.string.available_coin, String.valueOf(avaAmount), coin.toUpperCase()));
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
            amountEt.setText(mSender.getAmount());
            double amount = mCurrentWallet.getBalances(mSender.getCoin()).getAmount() / 100000.0;
            gxsTv.setText(getString(R.string.available_coin, String.valueOf(amount), mSender.getCoin()));
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示菜单
     *
     * @param items
     * @param checkedIndex
     */
    private void showMenuDialog(String[] items, int checkedIndex) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    mCurrentWallet = mWalletDataList.get(which);
                    mPresenter.fetchWalletBalance(mCurrentWallet);
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
        String from = fromTv.getText().toString();
        String to = toEt.getText().toString();
        String amount = amountEt.getText().toString();
        String memo = memoEt.getText().toString();
        mPresenter.send(from, to, amount, memo);
    }

    @OnClick(R.id.switch_account_send)
    void onSwitchAccountBtnClick() {
        showMenuDialog(mWalletNames, getCurrentWalletIndex());
    }

    /**
     * 获取当前钱包的索引
     *
     * @return 当前钱包的索引
     */
    private int getCurrentWalletIndex() {
        for (int i = 0; i < mWalletDataList.size(); i++) {
            if (mCurrentWallet.getName().equals(mWalletDataList.get(i).getName())) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onSendSuccess() {

    }

    @Override
    public void onFetchWalletBalanceSuccess(WalletData wallet, List<AccountBalance> balances) {
        wallet.setBalances(AssetSymbol.GXS, balances.get(1));
        AccountBalance balance = new AccountBalance();
        balance.setAmount(0);
        balance.setAssetId("1.3.1");
        wallet.setBalances(AssetSymbol.BTS, balance);
        initFromAccountWidget();
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

}
