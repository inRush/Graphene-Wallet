package com.sxds.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author inrush
 * @date 2017/7/21.
 * @package me.inrush.common.app
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment {

    // 根布局
    protected View mRoot;
    protected Unbinder mRootUnbinder;
    protected SparseArray<QMUITipDialog> mLoadings = new SparseArray<>();
    private int mCurrentLoadingId = 0;

    protected int generateLoadingId() {
        return mCurrentLoadingId++;
    }
    /**
     * 在Fragment添加到Activity中去的时候
     * @param context 上下文
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的根布局,但是不在创建时添加到container里面去
            View root = inflater.inflate(layId, container, false);
            initWidget(root);

            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                // 将当前的根布局从其父布局中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 当View创建完成以后初始化数据
        initData();
    }

    /**
     * 初始化相关参数
     * @param bundle 参数bundle
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 获取当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root) {
        mRootUnbinder = ButterKnife.bind(this,root);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    protected void showLoading(int code, String message) {
        final QMUITipDialog dialog = getDialog(QMUITipDialog.Builder.ICON_TYPE_LOADING, message);
        dialog.show();
        mLoadings.put(code, dialog);
    }

    protected void dismissLoading(int code) {
        QMUITipDialog dialog = mLoadings.get(code);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void showOk(String message) {
        final QMUITipDialog dialog = getDialog(QMUITipDialog.Builder.ICON_TYPE_SUCCESS, message);
        dialog.show();
        Run.onBackground(new Action() {
            @Override
            public void call() {
                try {
                    Thread.sleep(2000);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void showError(String message) {
        final QMUITipDialog dialog = getDialog(QMUITipDialog.Builder.ICON_TYPE_FAIL, message);
        dialog.show();
        Run.onBackground(new Action() {
            @Override
            public void call() {
                try {
                    Thread.sleep(2000);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private QMUITipDialog getDialog(int iconType, String message) {
        return new QMUITipDialog.Builder(getActivity())
                .setIconType(iconType)
                .setTipWord(message)
                .create();
    }

    /**
     * 返回按键触发时调用
     * @return 返回True代表已经处理返回逻辑,Activity不用自己finish.返回False代表没有处理返回逻辑,Activity自己走自己的逻辑
     */
    public boolean onBackPressed(){
        return false;
    }
}
