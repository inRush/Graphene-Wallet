package com.sxds.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import com.sxds.common.presenter.BaseContract;


/**
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.common.app
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter>
        extends BaseFragment
        implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    /**
     * 初始化一个Presenter
     *
     * @return 新建一个Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化Presenter
        initPresenter();
    }

    @Override
    public void showLoading(int code, int strRes) {
        showLoading(code, getString(strRes));
    }

    @Override
    public void showOk(@StringRes int strRes) {
        super.showOk(strRes);
    }

    @Override
    public void showInfo(int strRes) {
        showInfo(getString(strRes));
    }

    @Override
    public void showError(int strRes) {
        showError(getString(strRes));
    }

    @Override
    public void showError(String message) {
        super.showError(message);
    }

    @Override
    public void dismissAllLoading() {
        super.dismissAllLoading();
    }

    @Override
    public void dismissLoading(int code) {
        super.dismissLoading(code);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
