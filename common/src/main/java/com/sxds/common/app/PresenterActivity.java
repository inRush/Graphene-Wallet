package com.sxds.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

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
