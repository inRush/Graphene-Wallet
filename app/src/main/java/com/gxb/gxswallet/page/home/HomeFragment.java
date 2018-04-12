package com.gxb.gxswallet.page.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVGParseException;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.config.AssetSymbol;
import com.gxb.gxswallet.config.Configure;
import com.gxb.gxswallet.db.coin.CoinData;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.choose_coin.ChooseCoinActivity;
import com.gxb.gxswallet.page.cointransaction.CoinTransactionHistoryActivity;
import com.gxb.gxswallet.page.createwallet.CreateWalletActivity;
import com.gxb.gxswallet.page.home.adapter.CoinRecyclerAdapter;
import com.gxb.gxswallet.page.home.adapter.DrawerWalletRecyclerAdapter;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.gxb.gxswallet.page.importaccount.ImportWalletActivity;
import com.gxb.gxswallet.page.main.ExchangeServiceProvider;
import com.gxb.gxswallet.page.quotation.adapter.ExchangeRecyclerAdapter;
import com.gxb.gxswallet.page.receive.ReceiveActivity;
import com.gxb.gxswallet.page.send.SendActivity;
import com.gxb.gxswallet.page.send.model.Sender;
import com.gxb.gxswallet.receiver.EventActionCode;
import com.gxb.gxswallet.receiver.EventReceiver;
import com.gxb.gxswallet.receiver.OnEventReceiverListenerImpl;
import com.gxb.gxswallet.utils.CodeUtil;
import com.gxb.gxswallet.utils.jdenticon.Jdenticon;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.jwsd.libzxing.OnQRCodeScanCallback;
import com.jwsd.libzxing.QRCodeManager;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sxds.common.app.PresenterFragment;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/14.
 */

public class HomeFragment extends PresenterFragment<HomeContract.Presenter>
        implements HomeContract.View, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.avatar_home)
    ImageView avatarIv;
    @BindView(R.id.name_home)
    TextView nameTv;
    @BindView(R.id.coin_recycler_home)
    RecyclerView coinRecycler;
    RecyclerView accountRecycler;
    @BindView(R.id.drawer_layout_home)
    DrawerLayout drawerDl;
    @BindView(R.id.nav_menu_home)
    NavigationView navMenuNv;
    @BindView(R.id.total_assets_home)
    TextView totalAssets;
    @BindView(R.id.refresh_layout_home)
    SwipeRefreshLayout mRefreshLayout;

    private static final String ZERO_STR = "0.00";
    private ExchangeRecyclerAdapter mAdapter;
    private DrawerWalletRecyclerAdapter mDrawerWalletRecyclerAdapter;
    private CoinRecyclerAdapter mCoinRecyclerAdapter;
    private QMUITipDialog mLoadingDialog;

    private WalletData mCurrentWallet;
    private List<CoinItem> mCoinItems;
    private List<CoinData> mCoinDatas;
    private EventReceiver mExchangeChangeReceiver;
    private ExchangeServiceProvider mExchangeServiceProvider;
    private static final int REQUEST_CODE = CodeUtil.getCode();

    private DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected HomeContract.Presenter initPresenter() {
        return new HomePresenter(this, getContext());
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        List<WalletData> wallets = mPresenter.fetchWallet();
        loadWallet(wallets.get(0));
        initCoinList();
        initRefreshLayout();
        navMenuNv.setNavigationItemSelectedListener(this);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(() -> loadWallet(mCurrentWallet));
    }

    private void initCoinList() {
        mCoinDatas = mPresenter.fetchSupportCoin();
        mCoinItems = mPresenter.convertToCoinItem(mCoinDatas);
        mCoinRecyclerAdapter = new CoinRecyclerAdapter(mCoinItems);
        coinRecycler.setAdapter(mCoinRecyclerAdapter);
        coinRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mCoinRecyclerAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<CoinItem>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, CoinItem data) {
                super.onItemClick(holder, data);
                CoinTransactionHistoryActivity.start(getActivity(), mCurrentWallet, mCoinDatas.get(holder.getAdapterPosition()));
            }
        });
    }

    private void loadWallet(WalletData wallet) {
        try {
            mCurrentWallet = wallet;
            nameTv.setText(mCurrentWallet.getName());
            avatarIv.setImageDrawable(Jdenticon.from(mCurrentWallet.getName()).drawable());
            mPresenter.fetchWalletBalance(mCurrentWallet);
            mRefreshLayout.setRefreshing(true);
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    private void initDrawerWallet(List<WalletData> wallets) {
        if (mDrawerWalletRecyclerAdapter == null) {
            accountRecycler = navMenuNv
                    .inflateHeaderView(R.layout.fragment_home_drawer_header)
                    .findViewById(R.id.recycler_drawer_header);
            mDrawerWalletRecyclerAdapter = new DrawerWalletRecyclerAdapter(wallets);
            accountRecycler.setAdapter(mDrawerWalletRecyclerAdapter);
            accountRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            mDrawerWalletRecyclerAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<WalletData>() {
                @Override
                public void onItemClick(RecyclerAdapter.ViewHolder holder, WalletData data) {
                    super.onItemClick(holder, data);
                    loadWallet(data);
                    drawerDl.closeDrawer(Gravity.START);
                }
            });
        } else {
            mDrawerWalletRecyclerAdapter.replace(wallets);
        }
    }

    @OnClick(R.id.qrcode_home)
    void onQrCodeBtnClick() {
        ReceiveActivity.start(getActivity(), mCurrentWallet, mCoinDatas.get(0));
    }

    @SuppressLint("RtlHardcoded")
    @OnClick(R.id.nav_menu_btn_home)
    void onNavBtnClick() {
        drawerDl.openDrawer(Gravity.LEFT);
    }

    @OnClick(R.id.choose_coin_home)
    void onChooseCoinBtnClick() {
        ChooseCoinActivity.start(this, REQUEST_CODE);
    }


    private void setCoinItemsData(List<AccountBalance> balances) {
        for (int i = 0; i < mCoinItems.size(); i++) {
            AccountBalance balance = filterBalance(mCoinItems.get(i).getAssetId(), balances);
            if (balance != null) {
                mCoinItems.get(i).setAmount(balance.getAmount() / 100000.0);
                mCoinItems.get(i).setPrice(0);
            }
        }
        totalAssets.setText(getString(R.string.total_assets, "0.0"));
    }

    private AccountBalance filterBalance(String assetId, List<AccountBalance> balances) {
        for (AccountBalance balance : balances) {
            if (balance.getAssetId().equals(assetId)) {
                return balance;
            }
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onFetchWalletBalanceSuccess(WalletData wallet, List<AccountBalance> balances) {
        wallet.setBalances(AssetSymbol.GXS, balances.get(1));
        AccountBalance balance = new AccountBalance();
        balance.setAmount(0);
        balance.setAssetId("1.3.1");
        wallet.setBalances(AssetSymbol.BTS, balance);
        setCoinItemsData(balances);
        mCoinRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void showError(String str) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        super.showError(str);
    }

    /**
     * 计算币种的价值
     *
     * @param bestPrice 所有交易所最佳的价格
     * @return 全部币种的价格
     */
    public double calculateCoinPrice(double bestPrice) {
        double totalPrice = 0;
        for (int i = 0; i < mCoinItems.size(); i++) {
            CoinItem coinItem = mCoinItems.get(i);
            String name = coinItem.getName();
            double price = 0;
            if (AssetSymbol.GXS.equals(name)) {
                price = coinItem.getAmount() * bestPrice;
            }
            totalPrice += price;
            coinItem.setPrice(price);
        }
        return totalPrice;
    }

    /**
     * 初始化Exchange 发生变化的广播
     */
    private void initExchangeChangeReceiver() {
        mExchangeChangeReceiver = new EventReceiver(new OnEventReceiverListenerImpl() {
            @Override
            public void onExchangeListChange() {
                super.onExchangeListChange();
                if (mExchangeServiceProvider.getService() == null) {
                    return;
                }
                final double totalPrice = calculateCoinPrice(mExchangeServiceProvider.getService().getBestPriceRmb());
                Run.onUiSync(() -> {
                    mCoinRecyclerAdapter.notifyDataSetChanged();
                    totalAssets.setText(getString(R.string.total_assets, df.format(totalPrice)));
                    if (mRefreshLayout.isRefreshing() && mExchangeServiceProvider.getService().getBestPriceRmb() != 0) {
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventActionCode.ACTION_CODE_EXCHANGE_LIST_CHANGE);
        if (getActivity() != null) {
            getActivity().registerReceiver(mExchangeChangeReceiver, intentFilter);
        }
    }


    /**
     * 获取MainActivity中的ExchangeServiceProvider
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mExchangeServiceProvider = (ExchangeServiceProvider) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        initExchangeChangeReceiver();
        initDrawerWallet(mPresenter.fetchWallet());
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (mExchangeChangeReceiver != null && getActivity() != null) {
                // 反注册广播接收器
                getActivity().unregisterReceiver(mExchangeChangeReceiver);
                mExchangeChangeReceiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_drawer:
                onScanQR();
                break;
            case R.id.create_wallet_drawer:
                CreateWalletActivity.start(getActivity());
                break;
            case R.id.import_account_drawer:
                onImportWallet();
                break;
            default:
                break;
        }
        return true;
    }

    private void onImportWallet() {
        ImportWalletActivity.start(getActivity());
    }

    public void onScanQR() {
        QRCodeManager.getInstance()
                .with(getActivity())
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
                                SendActivity.start(getActivity(), new Sender(mCurrentWallet, account, amount, coin));
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
                        App.showToast(R.string.cancel_scan);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ChooseCoinActivity.RESULT_CODE) {
            initCoinList();
            loadWallet(mCurrentWallet);
        }
    }
}
