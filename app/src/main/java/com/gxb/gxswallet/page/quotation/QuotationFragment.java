package com.gxb.gxswallet.page.quotation;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.webview.RefreshAgentWebActivity;
import com.gxb.gxswallet.page.main.ExchangeServiceProvider;
import com.gxb.gxswallet.page.quotation.adapter.ExchangeRecyclerAdapter;
import com.gxb.gxswallet.page.quotationdetail.QuotationDetailActivity;
import com.gxb.gxswallet.receiver.EventActionCode;
import com.gxb.gxswallet.receiver.EventReceiver;
import com.gxb.gxswallet.receiver.OnEventReceiverListenerImpl;
import com.gxb.sdk.models.GXSExchange;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.app.PresenterFragment;
import com.sxds.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/24.
 */

public class QuotationFragment extends PresenterFragment<QuotationContract.Presenter>
        implements QuotationContract.View {
    @BindView(R.id.recycler_quotation)
    RecyclerView mExchangeList;
    @BindView(R.id.topbar_quotation)
    QMUITopBar mTopBar;

    private EventReceiver mExchangeChangeReceiver;
    private ExchangeServiceProvider mExchangeServiceProvider;
    private ExchangeRecyclerAdapter mExchangeRecyclerAdapter;
    private int loadingCode = generateLoadingId();

    @Override
    protected QuotationContract.Presenter initPresenter() {
        return new QuotationPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_quotation;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initTopBar();
        mExchangeRecyclerAdapter = new ExchangeRecyclerAdapter(new ArrayList<>());
        mExchangeList.setAdapter(mExchangeRecyclerAdapter);
        mExchangeList.setLayoutManager(new LinearLayoutManager(getContext()));
        mExchangeRecyclerAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<GXSExchange>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, GXSExchange data) {
                super.onItemClick(holder, data);
                QuotationDetailActivity.start(getActivity(), data);
            }
        });
        showLoading(loadingCode, getString(R.string.get_quotation));
    }

    private void initTopBar() {
        mTopBar.setTitle(getString(R.string.quotation));
        mTopBar.addRightImageButton(R.drawable.feixiaohao,View.generateViewId()).setOnClickListener(v -> RefreshAgentWebActivity.start(getActivity(), "https://m.feixiaohao.com/"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mExchangeServiceProvider = (ExchangeServiceProvider) context;
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
                dismissLoading(loadingCode);
                mExchangeRecyclerAdapter.replace(mExchangeServiceProvider.getService().getGxsExchanges());
                Run.onUiSync(() -> mExchangeRecyclerAdapter.notifyDataSetChanged());
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventActionCode.ACTION_CODE_EXCHANGE_LIST_CHANGE);
        if (getActivity() != null) {
            getActivity().registerReceiver(mExchangeChangeReceiver, intentFilter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initExchangeChangeReceiver();
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
}
