package com.sxds.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sxds.common.presenter.BaseContract;

/**
 * @author inrush
 * @date 2018/3/13.
 */

public abstract class PresenterActivity<Presenter extends BaseContract.Presenter>
        extends BaseActivity
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    /**
     * 初始化一个Presenter
     *
     * @return 新建一个Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showError(final String str) {
        super.showError(str);
    }

    @Override
    public void showLoading(final int code, final String str) {
        super.showLoading(code, str);
    }

    @Override
    public void showOk(final String str) {
        super.showOk(str);
    }

    @Override
    public void showInfo(final String str) {
        super.showInfo(str);
    }

    @Override
    public void showError(int strRes) {
        showError(getString(strRes));
    }

    @Override
    public void showOk(int strRes) {
        showOk(getString(strRes));
    }

    @Override
    public void showInfo(int strRes) {
        showInfo(getString(strRes));
    }

    @Override
    public void showLoading(int code, int strRes) {
        showLoading(code, getString(strRes));
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
