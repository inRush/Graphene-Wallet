package com.sxds.common.widget.recycler;

import android.util.SparseArray;

/**
 * RecycleView Item类型映射
 *
 * @author inrush
 * @date 2017/9/22.
 * @package com.sxds.oaplat.components.chartlist
 */

public interface TypeMapPolicy {
    /**
     * 类型映射
     */
    SparseArray<BaseRecyclerItemView> TYPES_MAPPING = new SparseArray<>();
    /**
     * 获取对应的布局
     *
     * @param viewFactory ViewFactory
     * @return 布局ID
     */
    int toType(BaseRecyclerItemView viewFactory);

    /**
     * 根据布局ID获取Item
     *
     * @param type 布局ID
     * @return ItemFactory
     */
    BaseRecyclerItemView toItemView(int type);
}
