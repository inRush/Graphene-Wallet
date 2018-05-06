package com.sxds.common.presenter;

import android.support.annotation.StringRes;

/**
 * MVP模式中公共的基本的契约
 *
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.presenter
 */

public class BaseContract {
    public interface View<T extends Presenter> {


        /**
         * 显示一个加载框
         *
         * @param code   加载框代号
         * @param strRes 需要显示的字符串资源
         */
        void showLoading(int code, @StringRes int strRes);

        /**
         * 关闭所有加载框
         */
        void dismissAllLoading();

        /**
         * 关闭对应的加载框
         *
         * @param code 加载框代号
         */
        void dismissLoading(int code);

        /**
         * 错误消息
         *
         * @param strRes
         */
        void showError(@StringRes int strRes);

        /**
         * 错误消息(显示字符串)
         *
         * @param message 需要显示的消息
         */
        void showError(String message);

        /**
         * 成功消息
         *
         * @param strRes 需要显示的字符串资源
         */
        void showOk(@StringRes int strRes);

        /**
         * 提示消息
         *
         * @param strRes 需要显示的字符串资源
         */
        void showInfo(@StringRes int strRes);

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
