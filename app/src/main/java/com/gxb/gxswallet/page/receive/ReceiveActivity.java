package com.gxb.gxswallet.page.receive;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.config.Configure;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.jwsd.libzxing.QRCodeManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.sxds.common.app.PresenterActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/17.
 */

public class ReceiveActivity extends PresenterActivity<ReceiveContract.Presenter>
        implements ReceiveContract.View {
    @BindView(R.id.amount_receive)
    EditText amountEt;
    @BindView(R.id.qrcode_receive)
    ImageView qrCodeIv;
    @BindView(R.id.account_name_receive)
    TextView accountNameTv;
    @BindView(R.id.topbar_reveive)
    QMUITopBar mTopBar;
    @BindView(R.id.avatar_receive)
    ImageView avatarIv;

    private static final String WALLET_KEY = "wallet_receive";
    private static final String COIN_KEY = "coin_receive";
    private ClipboardManager mClipboard;
    private WalletData mCurrentWallet;
    private AssetData mCurrentCoin;
    private QMUIListPopup mListPopup;

    private List<WalletData> mWalletDataList;
    private List<AssetData> mCoinDataList;
    private String[] mWalletNames;
    private String[] mCoinNames;

    public static void start(Activity activity, WalletData wallet, AssetData coin) {
        Intent intent = new Intent(activity, ReceiveActivity.class);
        intent.putExtra(WALLET_KEY, wallet);
        intent.putExtra(COIN_KEY, coin);
        activity.startActivity(intent);
    }

    @Override
    protected ReceiveContract.Presenter initPresenter() {
        return new ReceivePresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_receive;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mCurrentWallet = bundle.getParcelable(WALLET_KEY);
            mCurrentCoin = bundle.getParcelable(COIN_KEY);
            return super.initArgs(bundle);
        }
        App.showToast(R.string.account_name_error);
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mWalletDataList = mPresenter.fetchWallet();
        mCoinDataList = mPresenter.fetchSupportCoin();
        mWalletNames = new String[mWalletDataList.size()];
        mCoinNames = new String[mCoinDataList.size()];
        for (int i = 0; i < mWalletDataList.size(); i++) {
            mWalletNames[i] = mWalletDataList.get(i).getName();
        }
        for (int i = 0; i < mCoinDataList.size(); i++) {
            mCoinNames[i] = mCoinDataList.get(i).getName();
        }
    }

    private String generateQrcodeString(double amount) {
        return String.format(Locale.CHINA, "%sto=%s&amount=%.5f&coin=%s", Configure.QR_CODE_PRE_FIX, mCurrentWallet.getName(), amount
                , mCurrentCoin.getName());
    }

    private void setQrcode(double amount) {
        amountEt.setHint(getString(R.string.amount_receive, mCurrentCoin.getName()));
        Bitmap qrcode = QRCodeManager.getInstance().createQRCode(generateQrcodeString(amount), 500, 500);
        qrCodeIv.setImageBitmap(qrcode);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        initWalletMessage();
        initQrcode();
    }

    private void initQrcode() {
        setQrcode(0);
        amountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String amount = amountEt.getText().toString();
                if (".".equals(amount)) {
                    amountEt.setText("");
                    return;
                }
                if ("".equals(amount)) {
                    amount = "0";
                }
                setQrcode(Double.parseDouble(amount));
            }
        });

    }

    private void initWalletMessage() {
        try {
            avatarIv.setImageDrawable(Jdenticon.from(mCurrentWallet.getName()).drawable());
            accountNameTv.setText(mCurrentWallet.getName());
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }

    }

    private void initTopBar() {
        mTopBar.setTitle(getString(R.string.receive));
        mTopBar.addLeftBackImageButton().setOnClickListener(view -> finish());
        mTopBar.addRightImageButton(R.drawable.ic_more_vert_white_24dp, View.generateViewId())
                .setOnClickListener(view -> {
                    initListPopupIfNeed();
                    mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_RIGHT);
                    mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
                    mListPopup.show(view);
                });
    }

    @OnClick(R.id.copy_account_name_receive)
    void onCopyNameBtnClick() {
        ClipData clipData = ClipData.newPlainText("AccountName", mCurrentWallet.getName());
        mClipboard.setPrimaryClip(clipData);
        App.showToast(R.string.copy_success);
    }

    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
                    getString(R.string.switch_account),
                    getString(R.string.switch_coin),
            };
            List<String> data = new ArrayList<>();

            Collections.addAll(data, listItems);

            ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.receive_list_item, data);
            mListPopup = new QMUIListPopup(this, QMUIPopup.DIRECTION_NONE, adapter);

            mListPopup.setPopupLeftRightMinMargin(QMUIDisplayHelper.dp2px(this, -10));
            mListPopup.create(QMUIDisplayHelper.dp2px(this, 250), QMUIDisplayHelper.dp2px(this, 200),
                    (adapterView, view, i, l) -> {
                        mListPopup.dismiss();
                        int index = 0;
                        String[] menus = new String[0];
                        switch (i) {
                            case 0:
                                menus = mWalletNames;
                                index = getCurrentWalletIndex();
                                break;
                            case 1:
                                menus = mCoinNames;
                                index = getCurrentCoinIndex();
                                break;
                            default:
                                break;
                        }
                        showMenuDialog(menus, index, i);
                    });
        }
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

    /**
     * 获取当前币种的索引
     *
     * @return 当前币种的索引
     */
    private int getCurrentCoinIndex() {
        for (int i = 0; i < mCoinDataList.size(); i++) {
            if (mCurrentCoin.getName().equals(mCoinDataList.get(i).getName())) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 显示菜单
     *
     * @param items
     * @param checkedIndex
     */
    private void showMenuDialog(String[] items, int checkedIndex, final int type) {
        new QMUIDialog.CheckableDialogBuilder(this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    switch (type) {
                        case 0:
                            mCurrentWallet = mWalletDataList.get(which);
                            initWalletMessage();
                            break;
                        case 1:
                            mCurrentCoin = mCoinDataList.get(which);
                            break;
                        default:
                            break;
                    }
                    setQrcode(0);
                })
                .show();
    }

}
