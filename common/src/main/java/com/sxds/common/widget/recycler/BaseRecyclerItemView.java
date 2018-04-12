package com.sxds.common.widget.recycler;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base Recycle Item View
 *
 * @author inrush
 * @date 2017/9/22.
 */

public abstract class BaseRecyclerItemView<D, H extends RecyclerView.ViewHolder> {
    /**
     * item 的数据模型
     */
    protected D mData;
    /**
     * 对应RecyclerView的ViewHolder
     */
    private RecyclerView.Adapter mAdapter;
    /**
     * 上下文环境
     */
    private Context mContext;
    /**
     * item的位置
     */
    private int mPosition;

    public BaseRecyclerItemView(Context context, D data) {
        this.mData = data;
        this.mContext = context;
    }

    /**
     * @param parent   父容器
     * @param layoutId 布局ID
     * @return ViewHolder
     */
    public H innerCreateVH(ViewGroup parent, int layoutId) {
        return onCreateViewHolder(mContext, parent, layoutId);
    }

    /**
     * 视图数据绑定
     *
     * @param holder   ViewHolder
     * @param position 数据位置
     */
    public void innerBindVH(RecyclerView.ViewHolder holder, int position) {
        mPosition = position;
        onBindViewHolder(mContext, ((H) holder), mData);
    }

    public void attachAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 更新数据
     *
     * @param data D
     */
    public void refresh(D data) {
        resetData(data);
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(mPosition);
        }
    }

    /**
     * 重设数据
     *
     * @param data D
     */
    private void resetData(D data) {
        this.mData = data;
    }

    protected View inflateView(@LayoutRes int layoutId, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
    }

    /**
     * 创建ViewHolder时调用
     *
     * @param context  上下文
     * @param parent   parent
     * @param layoutId 布局ID
     * @return ViewHolder
     */
    public abstract H onCreateViewHolder(Context context, ViewGroup parent, int layoutId);

    /**
     * 绑定ViewHolder是调用
     *
     * @param context 上下文
     * @param holder  ViewHolder
     * @param data    数据
     */
    public abstract void onBindViewHolder(Context context, H holder, D data);
}
