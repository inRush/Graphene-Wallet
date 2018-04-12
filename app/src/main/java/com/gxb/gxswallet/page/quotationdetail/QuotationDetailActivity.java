package com.gxb.gxswallet.page.quotationdetail;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.NoScrollViewPage;
import com.gxb.gxswallet.base.activity.ExchangeServicePresenterActivity;
import com.gxb.gxswallet.page.quotationdetail.provider.ExchangeProvider;
import com.gxb.gxswallet.page.quotationdetail.tabfragments.DayKLineTabFragment;
import com.gxb.gxswallet.page.quotationdetail.tabfragments.MinuteTabFragment;
import com.gxb.gxswallet.receiver.EventActionCode;
import com.gxb.gxswallet.receiver.EventReceiver;
import com.gxb.gxswallet.receiver.OnEventReceiverListenerImpl;
import com.gxb.gxswallet.services.exchange.ExchangeService;
import com.gxb.sdk.models.GXSExchange;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/25.
 */

public class QuotationDetailActivity extends ExchangeServicePresenterActivity<QuotationDetailContract.Presenter>
        implements QuotationDetailContract.View, ExchangeProvider {
    @BindView(R.id.topbar_quotation_detail)
    QMUITopBar mTopBar;
    @BindView(R.id.highest_price_quotation_detail)
    TextView mHighestPriceTv;
    @BindView(R.id.minimum_price_quotation_detail)
    TextView mMinimumPriceTv;
    @BindView(R.id.volume_quotation_detail)
    TextView mVolumeTv;
    @BindView(R.id.tab_segment_quotation)
    QMUITabSegment mTabSegment;
    @BindView(R.id.content_viewPager_quotation)
    NoScrollViewPage mContentViewPage;
    @BindView(R.id.symbol_exchange)
    TextView mSymbolNameTv;
    @BindView(R.id.name_exchange)
    TextView mNameTv;
    @BindView(R.id.price_exchange)
    TextView mPriceTv;
    @BindView(R.id.price_rmb_exchange)
    TextView mRmbPriceTv;
    @BindView(R.id.quote_exchange)
    TextView mQuoteTv;

    private PagerAdapter mPagerAdapter;


    private ContentPage mDestPage = ContentPage.Time;
    private static final String EXCHANGE_KEY = "exchange";
    private GXSExchange mExchange;
    private EventReceiver mExchangeChangeReceiver;

    public static void start(Activity activity, GXSExchange exchange) {
        Intent intent = new Intent(activity, QuotationDetailActivity.class);
        intent.putExtra(EXCHANGE_KEY, exchange);
        activity.startActivity(intent);
    }


    @Override
    protected QuotationDetailContract.Presenter initPresenter() {
        return new QuotationDetailPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_quotation_detail;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            mExchange = bundle.getParcelable(EXCHANGE_KEY);
        }
        return super.initArgs(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initTopBar();
        initTabAndViewPage();
        initTopExchangePart();
    }


    private void initTopExchangePart() {
        if (mExchange == null) {
            return;
        }
        mSymbolNameTv.setText(mExchange.getSymbol());
        mNameTv.setText(mExchange.getName());
        mPriceTv.setText(mExchange.getPrice());
        mRmbPriceTv.setText(String.format("￥%s", mExchange.getPrice_rmb()));
        String quote;
        if (mExchange.getQuote() < 0) {
            mQuoteTv.setBackgroundColor(Color.parseColor("#c52225"));
            quote = String.valueOf(mExchange.getQuote()) + "%";
        } else {
            mQuoteTv.setBackgroundColor(Color.parseColor("#165d78"));
            quote = "+" + String.valueOf(mExchange.getQuote()) + "%";

        }
        mQuoteTv.setText(quote);
        mHighestPriceTv.setText(mExchange.getHigh());
        mMinimumPriceTv.setText(mExchange.getLow());
        mVolumeTv.setText(mExchange.getVolume());
    }

    private void initTopBar() {
        mTopBar.setTitle(getString(R.string.real_time_quotes));
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initTabAndViewPage() {
        final Fragment[] fragments = new Fragment[]{
                MinuteTabFragment.newInstance(mExchange),
                DayKLineTabFragment.newInstance(null)
        };
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        };

        mContentViewPage.setAdapter(mPagerAdapter);
        mContentViewPage.setCurrentItem(mDestPage.getPosition(), false);
        mTabSegment.addTab(new QMUITabSegment.Tab(getString(R.string.minute_by_minute)));
        mTabSegment.addTab(new QMUITabSegment.Tab(getString(R.string.day_k_line)));
        mTabSegment.setupWithViewPager(mContentViewPage, false, true);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                mTabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    @Override
    public GXSExchange getExchange() {
        return mExchange;
    }


    public enum ContentPage {
        /**
         * 分时
         */
        Time(0),
        /**
         * 日K图
         */
        DayKLine(1);
        public static final int SIZE = 2;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Time;
                case 1:
                    return DayKLine;
                default:
                    return Time;
            }
        }

        public int getPosition() {
            return position;
        }
    }

    @Override
    protected Intent setAction(Intent intent) {
        intent.putExtra(ExchangeService.RUN_TYPE_KEY, ExchangeService.RUN_TYPE_FETCH_ONE);
        intent.putExtra(ExchangeService.EXCHANGE_KEY, mExchange);
        return intent;
    }

    /**
     * 初始化Exchange 发生变化的广播
     */
    private void initExchangeChangeReceiver() {
        mExchangeChangeReceiver = new EventReceiver(new OnEventReceiverListenerImpl() {
            @Override
            public void onExchangeChange() {
                super.onExchangeChange();
                if (mExchangeService == null) {
                    return;
                }
                mExchange = mExchangeService.getGXSExchange();
                initTopExchangePart();
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventActionCode.ACTION_CODE_EXCHANGE_CHANGE);
        registerReceiver(mExchangeChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExchangeChangeReceiver != null) {
            unregisterReceiver(mExchangeChangeReceiver);
            mExchangeChangeReceiver = null;
        }
    }

    @Override
    protected void onConnectionSuccess() {
        super.onConnectionSuccess();
        initExchangeChangeReceiver();
    }
}
