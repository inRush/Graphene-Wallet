package com.gxb.gxswallet.page.walletmanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.activity.ExchangeServicePresenterActivity;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.createwallet.CreateWalletActivity;
import com.gxb.gxswallet.page.importaccount.ImportWalletActivity;
import com.gxb.gxswallet.page.walletdetail.WalletDetailActivity;
import com.gxb.gxswallet.page.walletmanager.adapter.WalletManagerRecyclerAdapter;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/27.
 */

public class WalletManagerActivity extends ExchangeServicePresenterActivity<WalletManagerContract.Presenter>
        implements WalletManagerContract.View {

    @BindView(R.id.topbar_wallet_manager)
    QMUITopBar mTopBar;
    @BindView(R.id.recycler_wallet_manager)
    RecyclerView mWalletList;

    private WalletManagerRecyclerAdapter mAdapter;
    private List<WalletData> mWallets;
    private int mCompleteCount = 0;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, WalletManagerActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected WalletManagerContract.Presenter initPresenter() {
        return new WalletManagerPresenter(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        initWalletList();
    }

    private void initWalletList() {
        mWallets = new ArrayList<>();
        mAdapter = new WalletManagerRecyclerAdapter(mWallets);
        mWalletList.setAdapter(mAdapter);
        mWalletList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<WalletData>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, WalletData data) {
                super.onItemClick(holder, data);
            }
        });


        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<WalletData>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, WalletData data) {
                super.onItemClick(holder, data);
                WalletDetailActivity.start(WalletManagerActivity.this, data);
            }
        });
    }

    private void initTopBar() {
        mTopBar.setTitle(getString(R.string.wallet_manager));
        mTopBar.addLeftBackImageButton().setOnClickListener(view -> finish());
    }

    @Override
    protected Intent setAction(Intent intent) {
        return null;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_wallet_manager;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWallets = mPresenter.fetchWallets();
        for (WalletData wallet : mWallets) {
            mPresenter.fetchWalletBalance(wallet);
        }
        mAdapter.replace(mWallets);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.create_wallet_wallet_manager)
    void onCreateWalletBtnClick() {
        CreateWalletActivity.start(this);
    }

    @OnClick(R.id.import_wallet_wallet_manager)
    void onImportWalletBtnClick() {
        ImportWalletActivity.start(this);
    }

    @Override
    public void onFetchWalletBalanceSuccess(WalletData wallet, List<AccountBalance> balances) {
//        wallet.setBalances(AssetSymbol.GXS.getName(), balances.get(1));
//        AccountBalance balance = new AccountBalance();
//        balance.setAmount(0);
//        balance.setAssetId("1.3.1");
//        wallet.setBalances(AssetSymbol.BTS.getName(), balance);
//        mCompleteCount += 1;
//        if (mCompleteCount == mWallets.size()) {
//            mAdapter.notifyDataSetChanged();
//        }
    }
}
