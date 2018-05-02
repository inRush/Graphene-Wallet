package com.gxb.gxswallet.page.transaction_history_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.page.cointransaction.model.TransactionHistory;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sxds.common.app.BaseActivity;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import cy.agorise.graphenej.PublicKey;

/**
 * @author inrush
 * @date 2018/4/5.
 */

public class TransactionHistoryDetailActivity extends BaseActivity implements View.OnClickListener {

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
            if (mTransactionHistory.getMemo() != null) {
                ECKey privateKey = DumpedPrivateKey.fromBase58(NetworkParameters.prodNet(), "5KQiXpeTfxvBe5AB4Q2ZdkhwPxTdyj4Y2abdk5W1qerRGoptMer").getKey();
                PublicKey publicKey = new PublicKey(ECKey.fromPublicOnly(privateKey.getPubKey()));
//                try {
//                    String message = Memo.decryptMessage(privateKey,publicKey,mTransactionHistory.getMemo().getNonce(),mTransactionHistory.getMemo().getByteMessage());
//                } catch (ChecksumException e) {
//                    e.printStackTrace();
//                }
            }
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

            } else if (partTwoSettings[1].equals(title)) {

            }
        }
    }



}
