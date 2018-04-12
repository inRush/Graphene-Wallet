package com.gxb.gxswallet.page.quotationdetail.tabfragments;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * 图表数据格式化器
 * @author inrush
 * @date 2017/9/23.
 * @package com.sxds.oaplat.components.chartlist.formatter
 */

public class Formatter implements IAxisValueFormatter {
    private String[] mXDatas;

    public Formatter(String[] xDatas) {
        this.mXDatas = xDatas;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        if (this.mXDatas == null || this.mXDatas.length <= index || index < 0) {
            return index + "";
        }
        return this.mXDatas[index];
    }
}
