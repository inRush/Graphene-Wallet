package com.sxds.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sxds.common.presenter.BaseContract;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @author inrush
 * @date 2018/3/13.
 */

public abstract class PresenterActivity<Presenter extends BaseContract.Presenter>
        extends BaseActivity
        implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;
    private int mCurrentLoadingId = 0;

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
        Run.onUiSync(new Action() {
            @Override
            public void call() {
                dismissAllLoading();
                final QMUITipDialog dialog = new QMUITipDialog.Builder(PresenterActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                        .setTipWord(str)
                        .create();
                dialog.show();
                Run.onBackground(new Action() {
                    @Override
                    public void call() {
                        try {
                            Thread.sleep(1500);
                            dialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int generateLoadingId() {
        return mCurrentLoadingId++;
    }

    @Override
    public void showLoading(final int code, final String str) {
        Run.onUiSync(new Action() {
            @Override
            public void call() {
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
