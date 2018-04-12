package com.gxb.gxswallet.page.quotationdetail.tabfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.page.quotationdetail.provider.ExchangeProvider;
import com.gxb.sdk.api.GxbApis;
import com.gxb.sdk.http.GxbCallBack;
import com.gxb.sdk.models.ExchangeLineData;
import com.gxb.sdk.models.GXSExchange;
import com.sxds.common.app.BaseFragment;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/25.
 */

public class MinuteTabFragment extends BaseFragment {


    @BindView(R.id.line_chart_minute)
    LineChart mLineChart;

    public static final String DATA_KEY = "line_data";
    private List<ExchangeLineData> mLineDataList;
    private GXSExchange mExchange;

    private ExchangeProvider mExchangeProvider;
    /**
     * 时间格式化器
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    private boolean runExchangeLoop = true;


    public static MinuteTabFragment newInstance(GXSExchange exchange) {
        MinuteTabFragment fragment = new MinuteTabFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MinuteTabFragment.DATA_KEY, exchange);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mExchangeProvider = (ExchangeProvider) context;
    }

    @Override
    public void onResume() {
        runExchangeLoop = true;
        runExchangeLoop();
        super.onResume();
    }

    @Override
    public void onPause() {
        runExchangeLoop = false;
        super.onPause();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_minute;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        if (bundle != null) {
            mExchange = bundle.getParcelable(DATA_KEY);
        }
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);
        initLegend();
        initXAxis();
        initLeftAxis();
        mLineChart.getAxisRight().setEnabled(false);
    }


    @Override
    protected void initData() {
        super.initData();
        GxbApis.getInstance()
                .transactionApi().getExchange(mExchange, 1, new GxbCallBack() {
            @Override
            public void onFailure(Error error) {

            }

            @Override
            public void onSuccess(String result) {
                mLineDataList = ExchangeLineData.fromJsonToList(result);
                refreshChartData();
            }
        });
    }

    private void runExchangeLoop() {
        Run.onBackground(new Action() {
            @Override
            public void call() {
                while (runExchangeLoop) {
                    if (mLineDataList == null || mLineDataList.size() == 0) {
                        continue;
                    }
                    mExchange = mExchangeProvider.getExchange();
                    Calendar nowTime = Calendar.getInstance();
                    mLineDataList.add(new ExchangeLineData(mExchange.getPrice(), dateFormat.format(nowTime.getTime())));
                    refreshChartData();
                    try {
                        // 一分钟获取一次
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setData() {
        ArrayList<Entry> yVals = new ArrayList<>();
        String[] xAxisDatas = new String[mLineDataList.size()];
        for (int i = 0; i < mLineDataList.size(); i++) {
            yVals.add(new Entry(i, Float.parseFloat(mLineDataList.get(i).getPrice())));
            xAxisDatas[i] = mLineDataList.get(i).getTime();
        }
        mLineChart.getXAxis().setValueFormatter(new Formatter(xAxisDatas));
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            LineDataSet set = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set.setValues(yVals);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            LineDataSet set = new LineDataSet(yVals, getString(R.string.price_trend));
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(getResources().getColor(R.color.colorPrimary));
            set.setDrawCircles(false);
            LineData lineData = new LineData(set);
            mLineChart.setData(lineData);
        }
    }

    private void refreshChartData() {
        setData();
        Run.onUiSync(new Action() {
            @Override
            public void call() {
                mLineChart.invalidate();
            }
        });
    }

    private void initXAxis() {
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setDrawGridLines(false);

    }

    private void initLeftAxis() {
        YAxis yAxis = mLineChart.getAxisLeft();
//        yAxis.setDrawLabels(false);
        yAxis.setDrawGridLines(false);
    }

    private void initLegend() {
        Legend l = mLineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);
    }

}
