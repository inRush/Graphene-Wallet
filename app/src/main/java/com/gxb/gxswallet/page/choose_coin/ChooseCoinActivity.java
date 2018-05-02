package com.gxb.gxswallet.page.choose_coin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.page.choose_coin.adapter.ChooseCoinRecyclerAdapter;
import com.gxb.gxswallet.page.home.model.CoinItem;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.gxb.gxswallet.utils.AssetsUtil;
import com.gxb.gxswallet.utils.CodeUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.app.BaseActivity;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/4/11.
 */

public class ChooseCoinActivity extends BaseActivity {

    @BindView(R.id.recycler_choose_coin)
    RecyclerView mCoinList;
    @BindView(R.id.topbar_choose_coin)
    QMUITopBar mTopBar;

    private ChooseCoinRecyclerAdapter mAdapter;
    private int mEnableAssetLoadingCode = generateLoadingId();
    private int mDisableAssetLoadingCode = generateLoadingId();
    public static final int RESULT_CODE = CodeUtil.getCode();

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ChooseCoinActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), ChooseCoinActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_choose_coin;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        mCoinList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initTopBar() {
        mTopBar.setTitle(getString(R.string.choose_enable_coin));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void initData() {
        super.initData();
        List<AssetData> coinDatas = AssetDataManager.getAll();
        mAdapter = new ChooseCoinRecyclerAdapter(convertToCoinItem(coinDatas));
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<CoinItem>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, CoinItem data) {
                super.onItemClick(holder, data);
                int pos = holder.getAdapterPosition();
                AssetData coinData = coinDatas.get(pos);
                if (coinData.isEnable()) {
                    showLoading(mDisableAssetLoadingCode, getString(R.string.disconnecting_data));
                    WebSocketServicePool.getInstance().disconnect(coinData.getName(),
                            () -> {
                                dismissLoading(mDisableAssetLoadingCode);
                                if (new AssetDataManager(ChooseCoinActivity.this).disableAsset(coinData)) {
                                    showOk(getString(R.string.disable_success));
                                } else {
                                    showError(getString(R.string.disable_failure));
                                }
                            });

                } else {
                    showLoading(mEnableAssetLoadingCode, getString(R.string.connecting_data));
                    WebSocketServicePool.getInstance().addConnect(coinData.getName(), coinData.getNets().get(0)
                            , new WebSocketServicePool.OnAddConnectListener() {
                                @Override
                                public void onConnectedSuccess() {
                                    dismissLoading(mEnableAssetLoadingCode);
                                    if (new AssetDataManager(ChooseCoinActivity.this).enableAsset(coinData)) {
                                        showOk(getString(R.string.enable_success));
                                    } else {
                                        showError(getString(R.string.enable_failure));
                                    }
                                }

                                @Override
                                public void onConnectedFailure() {
                                    dismissLoading(mEnableAssetLoadingCode);
                                    showError(getString(R.string.connect_error_please_retry));
                                    mAdapter.notifyItemChanged(pos);
                                }
                            });


                }
            }
        });
        mCoinList.setAdapter(mAdapter);
    }

    public List<CoinItem> convertToCoinItem(List<AssetData> coinDatas) {
        List<CoinItem> coinItems = new ArrayList<>();
        for (AssetData coinData : coinDatas) {
            Bitmap icon = AssetsUtil.getImage(coinData.getIcon());
            coinItems.add(new CoinItem(icon, coinData.getName(), 0, coinData.getAssetId(), 0, coinData.getEnable()));
        }
        return coinItems;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CODE);
        super.onBackPressed();
    }
}
