package com.sxds.common.presenter;

import android.support.annotation.StringRes;
import android.util.SparseArray;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * MVP模式中公共的基本的契约
 *
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.presenter
 */

public class BaseContract {
    public interface View<T extends Presenter> {
        SparseArray<QMUITipDialog> mLoadings = new SparseArray<>();

        /**
         * 显示一个字符串错误
         *
         * @param str
         */
        void showError(String str);


        /**
         * 显示一个进度条
         */
        void showLoading(int code, String str);

        /**
         * 根据resource ID显示一个进度条
         */
        void showLoading(int code, @StringRes int strRes);

        void dismissAllLoading();

        /**
         * 关闭顶的进度条
         *
         * @param code 代号
         */
        void dismissLoading(int code);


        /**
         * 生成一个loading ID
         * @return loading ID
         */
        int generateLoadingId();

        /**
         * 设置一个Presenter
         *
         * @param presenter
         */
        void setPresenter(T presenter);
    }

    public interface Presenter {
        /**
         * 启动方法
         */
        void start();

        /**
         * 销毁方法
         */
        void destroy();
    }
}
