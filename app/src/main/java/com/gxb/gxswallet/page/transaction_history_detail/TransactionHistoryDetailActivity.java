package com.gxb.gxswallet.page.transaction_history_detail;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.dialog.PasswordDialog;
import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.page.cointransaction.model.TransactionHistory;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.gxb.sdk.bitlib.util.StringUtils;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sxds.common.app.BaseActivity;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.graphenej.objects.Memo;

/**
 * @author inrush
 * @date 2018/4/5.
 */

public class TransactionHistoryDetailActivity extends BaseActivity
        implements View.OnClickListener, PasswordDialog.OnPasswordConfirmListener {

    @BindView(R.id.topbar_transaction_history_detail)
    QMUITopBar mTopBar;
    @BindView(R.id.avatar_transaction_history_detail)
    ImageView mAvatarIv;
    @BindView(R.id.amount_transaction_history_detail)
    TextView mAmountTv;
    @BindView(R.id.asset_transaction_history_detail)
    TextView mAssetIv;
    @BindView(R.id.groupListView_transaction_history_detail)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.account_transaction_history_detail)
    TextView mAccountTv;

    private TransactionHistory mTransactionHistory;
    private String[] partOneSettings;
    private String[] partTwoSettings;

    private static final String DATA_KEY = "history";

    public static void start(Activity activity, TransactionHistory history) {
        Intent intent = new Intent(activity, TransactionHistoryDetailActivity.class);
        intent.putExtra(DATA_KEY, history);
        activity.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mTransactionHistory = bundle.getParcelable(DATA_KEY);
            return super.initArgs(bundle);
        }
        return false;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_transaction_history_detail;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        initTransactionMessage();
        initGroupList();
    }

    private void initGroupList() {
        initMenus();
        QMUICommonListItemView sendAccountItem = mGroupListView.createItemView(partOneSettings[0]);
        QMUICommonListItemView receiveAccountItem = mGroupListView.createItemView(partOneSettings[1]);
        QMUICommonListItemView feeItem = mGroupListView.createItemView(partOneSettings[2]);
        QMUICommonListItemView operationTimeItem = mGroupListView.createItemView(partOneSettings[3]);
        sendAccountItem.setDetailText(mTransactionHistory.getFrom());
        receiveAccountItem.setDetailText(mTransactionHistory.getTo());
        feeItem.setDetailText(String.valueOf(mTransactionHistory.getFee()));
        operationTimeItem.setDetailText(mTransactionHistory.getDate());
        QMUIGroupListView.newSection(this)
                .setTitle("")
                .addItemView(sendAccountItem, this)
                .addItemView(receiveAccountItem, this)
                .addItemView(feeItem, this)
                .addItemView(operationTimeItem, this)
                .addTo(mGroupListView);

        QMUICommonListItemView memoItem = mGroupListView.createItemView(partTwoSettings[0]);
        QMUICommonListItemView txidItem = mGroupListView.createItemView(partTwoSettings[1]);
        memoItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        txidItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUIGroupListView.newSection(this)
                .setTitle("")
                .addItemView(memoItem, this)
                .addItemView(txidItem, this)
                .addTo(mGroupListView);
//        WalletService.getInstance().unlockWallet()
//        showOk(Memo.decryptMessage());
    }

    private void initMenus() {
        partOneSettings = new String[]{
                getString(R.string.send_account),
                getString(R.string.receive_account),
                getString(R.string.fee),
                getString(R.string.operation_time)
        };
        partTwoSettings = new String[]{
                getString(R.string.memo),
                getString(R.string.txid)
        };
    }


    private void initTransactionMessage() {
        try {
            if (mTransactionHistory.getType() == TransactionHistory.TransactionType.send) {
                mTopBar.setTitle(R.string.send_to_this_account);
                mAvatarIv.setImageDrawable(Jdenticon.from(mTransactionHistory.getTo()).drawable());
                mAmountTv.setText(String.format(Locale.CHINA, "- %f", mTransactionHistory.getAmount()));
                mAccountTv.setText(mTransactionHistory.getTo());
            } else {
                mTopBar.setTitle(R.string.from_this_account);
                mAvatarIv.setImageDrawable(Jdenticon.from(mTransactionHistory.getFrom()).drawable());
                mAmountTv.setText(String.format(Locale.CHINA, "+ %f", mTransactionHistory.getAmount()));
                mAccountTv.setText(mTransactionHistory.getFrom());
            }
            mAssetIv.setText(mTransactionHistory.getAsset());
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View v) {
        if (v instanceof QMUICommonListItemView) {
            String title = ((QMUICommonListItemView) v).getText().toString();
            if (partTwoSettings[0].equals(title)) {
                if (mTransactionHistory.getMemo() != null) {
                    showPasswordDialog();
                } else {
                    showInfo(R.string.this_transaction_not_have_memo);
                }
            } else if (partTwoSettings[1].equals(title)) {
                if(mTransactionHistory.getTransactionIds() == null){
                    showInfo(R.string.this_transaction_not_have_txid);
                }else {
                    showTxIdsDialog(mTransactionHistory.getTransactionIds());
                }
            }
        }
    }

    private void showPasswordDialog() {
        new PasswordDialog().setPasswordConfirmListener(this).show(getSupportFragmentManager(), "password");
    }

    private void showMemoDialog(ECKey privateKey, Memo memo) {
        try {
            String memoText = WalletService.getInstance().decryptMemo(privateKey, memo,
                    mTransactionHistory.getType() == TransactionHistory.TransactionType.send);
            CopyTextDialogBuilder dialog = new CopyTextDialogBuilder(this, getString(R.string.memo), memoText);
            dialog.show();
        } catch (Exception e) {
            showError(getString(R.string.parse_error));
        }

    }

    private void showTxIdsDialog(List<String> txids) {
        String[] ids = new String[txids.size()];
        txids.toArray(ids);
        CopyTextDialogBuilder dialog = new CopyTextDialogBuilder(this, "TXID", StringUtils.join(ids, ","));
        dialog.show();
    }

    @Override
    public void onConfirm(String password) {
        String[] datas = WalletService.getInstance().unlockWallet(WalletManager.getInstance().getCurrentWallet(), password);
        if (datas[0] == null) {
            showError(R.string.password_error);
        } else {
            showMemoDialog(DumpedPrivateKey.fromBase58(null, datas[0]).getKey(), mTransactionHistory.getMemo());
        }
    }

    class CopyTextDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {

        @BindView(R.id.text_dialog_show_copy_text)
        TextView mContentTv;
        @BindView(R.id.title_dialog_show_copy_text)
        TextView mTitleTv;
        private Context mContext;
        private String mText;
        private String mTitle;
        private ClipboardManager mClipboard;


        public CopyTextDialogBuilder(Context context, String title, String text) {
            super(context);
            mContext = context;
            this.mText = text;
            this.mTitle = title;
            mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        }


        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            View rootView = LayoutInflater.from(TransactionHistoryDetailActivity.this).inflate(R.layout.dialog_show_copy_text, parent, false);
            ButterKnife.bind(this, rootView);
            mContentTv.setText(mText);
            mTitleTv.setText(mTitle);
            return rootView;
        }

        @OnClick(R.id.btn_dialog_show_copy_text)
        void onBtnClick(View v) {
            ClipData clipData = ClipData.newPlainText("PrivateKey", mContentTv.getText().toString());
            mClipboard.setPrimaryClip(clipData);
            App.showToast(R.string.copy_success);
        }
    }


}
