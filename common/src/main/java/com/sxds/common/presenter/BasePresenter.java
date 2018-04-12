package com.sxds.common.presenter;

/**
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.presenter
 */

public class BasePresenter<T extends BaseContract.View>
        implements BaseContract.Presenter {

    private T mView;

    public BasePresenter(T view){
        setView(view);
    }

    /**
     * 设置一个View
     * @param view View
     */
    @SuppressWarnings("unchecked")
    protected void setView(T view){
        mView = view;
        mView.setPresenter(this);
    }

    /**
     * 给子类使用的获取View的操作
     * 不允许被复写
     * @return View
     */
    protected final T getView(){
        return mView;
    }
    @Override
    public void start() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if(view != null){
            view.setPresenter(null);
        }
    }
}
