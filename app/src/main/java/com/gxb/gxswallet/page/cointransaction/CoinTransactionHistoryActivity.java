package com.gxb.gxswallet.page.cointransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.cointransaction.adapter.TransactionHistoryAdapter;
import com.gxb.gxswallet.page.cointransaction.model.TransactionHistory;
import com.gxb.gxswallet.page.receive.ReceiveActivity;
import com.gxb.gxswallet.page.send.SendActivity;
import com.gxb.gxswallet.page.send.model.Sender;
import com.gxb.gxswallet.page.transaction_history_detail.TransactionHistoryDetailActivity;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.app.PresenterActivity;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/4/4.
 */

public class CoinTransactionHistoryActivity extends PresenterActivity<CoinTransactionHistoryContract.Presenter>
        implements CoinTransactionHistoryContract.View {

    @BindView(R.id.recycler_transaction_history)
    RecyclerView mTransactionHistoryList;
    @BindView(R.id.topbar_transaction_history)
    QMUITopBar mTopBar;
    @BindView(R.id.empty_view_transaction_history)
    QMUIEmptyView mEmptyView;

    private static final String DATA_KEY = "wallet";
    private static final String ASSET_DATA_KEY = "asset";
    private WalletData mWalletData;
    private AssetData mAsset;
    private int loadingId = generateLoadingId();
    private TransactionHistoryAdapter mHistoryAdapter;

    public static void start(Activity activity, WalletData wallet, AssetData assetItem) {
        Intent intent = new Intent(activity, CoinTransactionHistoryActivity.class);
        intent.putExtra(DATA_KEY, wallet);
        intent.putExtra(ASSET_DATA_KEY, assetItem);
        activity.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mWalletData = bundle.getParcelable(DATA_KEY);
            mAsset = bundle.getParcelable(ASSET_DATA_KEY);
        }
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        mHistoryAdapter = new TransactionHistoryAdapter(new ArrayList<>(), mAsset);
        mTransactionHistoryList.setLayoutManager(new LinearLayoutManager(this));
        mTransactionHistoryList.setAdapter(mHistoryAdapter);
        mHistoryAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<TransactionHistory>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, TransactionHistory data) {
                super.onItemClick(holder, data);
                TransactionHistoryDetailActivity.start(CoinTransactionHistoryActivity.this, data);
            }
        });
    }

    private void initTopBar() {
        mTopBar.setTitle(R.string.transaction_history);
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.addRightImageButton(R.drawable.ic_refresh_white_24dp, View.generateViewId())
                .setOnClickListener(v -> loadHistory());
    }

    @Override
    protected void initData() {
        super.initData();
        loadHistory();
    }

    private void loadHistory() {
        mPresenter.getTransactionHistory(mWalletData, mAsset);
        showLoading(loadingId, R.string.loading_transaction_list);
    }

    @Override
    protected CoinTransactionHistoryContract.Presenter initPresenter() {
        return new CoinTransactionHistoryPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_coin_transaction;
    }

    @Override
    public void onGetTransactionHistorySuccess(List<TransactionHistory> histories) {
        dismissLoading(loadingId);
        if (histories.size() == 0) {
            mEmptyView.show(getString(R.string.no_transaction_history), null);
        } else {
            mEmptyView.hide();
            mHistoryAdapter.replace(histories);
            mHistoryAdapter.notifyDataSetChanged();
            dismissLoading(loadingId);
        }
    }

    @OnClick(R.id.send_transaction_history)
    void onSendBtnClick() {
        SendActivity.start(this, new Sender(null, null, mAsset, ""));
    }

    @OnClick(R.id.receive_transaction_history)
    void onReceiveBtnClick() {
        ReceiveActivity.start(this, mWalletData, mAsset);
    }

}
