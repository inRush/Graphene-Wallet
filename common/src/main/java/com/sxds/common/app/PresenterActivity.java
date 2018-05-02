package com.sxds.common.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissAllLoading();
                QMUITipDialog dialog = new QMUITipDialog.Builder(PresenterActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(str)
                        .create();
                dialog.show();
                delayDismissDialog(dialog, 1500);
            }
        });
    }

    private void delayDismissDialog(final QMUITipDialog dialog, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delay);
    }


    @Override
    public void showLoading(final int code, final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                QMUITipDialog dialog = new QMUITipDialog.Builder(PresenterActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord(str)
                        .create();
                dialog.show();
                mLoadings.put(code, dialog);
            }
        });
    }

    @Override
    public void showOk(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                QMUITipDialog dialog = new QMUITipDialog.Builder(PresenterActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord(str)
                        .create();
                dialog.show();
                delayDismissDialog(dialog, 1500);
            }
        });
    }

    @Override
    public void showInfo(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                QMUITipDialog dialog = new QMUITipDialog.Builder(PresenterActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                        .setTipWord(str)
                        .create();
                dialog.show();
                delayDismissDialog(dialog, 1500);
            }
        });
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
        for (int i = 0; i < mLoadings.size(); i++) {
            QMUITipDialog dialog = mLoadings.valueAt(i);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void dismissLoading(int code) {
        super.dismissLoading(code);
        QMUITipDialog dialog = mLoadings.get(code);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
