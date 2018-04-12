package com.sxds.common.widget.recycler;

/**
 * 用于更新Adapter
 * @author inrush
 * @date 2017/7/21.
 * @package me.inrush.common.widget.recycler
 */

public interface AdapterCallback<T> {
    /**
     * 更新数据
     * @param data
     * @param holder
     */
    void update(T data, RecyclerAdapter.ViewHolder<T> holder);
}
