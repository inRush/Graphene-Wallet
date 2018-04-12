package com.gxb.gxswallet.page.quotationdetail.tabfragments;

import android.os.Bundle;

import com.gxb.gxswallet.R;
import com.sxds.common.app.BaseFragment;

/**
 * @author inrush
 * @date 2018/3/25.
 */

public class DayKLineTabFragment extends BaseFragment {

    public static DayKLineTabFragment newInstance(Bundle bundle) {
        DayKLineTabFragment fragment = new DayKLineTabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_day_k_line;
    }
}
