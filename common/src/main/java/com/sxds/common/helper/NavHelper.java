package com.sxds.common.helper;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;


/**
 * 解决Fragment的调度与重用问题,达到最优的Fragment切换
 *
 * @author inrush
 * @date 2017/8/5.
 * @package me.inrush.italker.helper
 */

public class NavHelper<T> {
    /**
     * 所有的tab集合
     */
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();

    /**
     * 用于初始化的必须参数
     */
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final Context context;
    private final OnTabChangedListener<T> listener;
    /**
     * 当前选中的tab
     */
    private Tab<T> currentTab;


    /**
     * 构造函数
     *
     * @param context         上下文
     * @param fragmentManager Fragment管理器
     * @param containerId     容器Id
     * @param listener        监听器
     */
    public NavHelper(Context context,
                     FragmentManager fragmentManager,
                     int containerId,
                     OnTabChangedListener<T> listener) {

        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context;
        this.listener = listener;
    }

    /**
     * 添加tab
     *
     * @param menuId 对应的菜单Id
     * @param tab    Tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }


    /**
     * 获取当前显示的tab
     *
     * @return 当前显示的tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     *
     * @param menuId 菜单的Id
     * @return 是否能够处理这个点击
     */
    public boolean performClickMenu(int menuId) {
        // 找出tabs中寻找menuId对应的tab
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行tab的处理
     *
     * @param tab 需要处理的tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                notifyTabReselect(tab);
                return;
            }
        }

        // 赋值currentTab,并切换tab
        currentTab = tab;
        doTabChanged(currentTab, oldTab);

    }

    /**
     * 对tab进行切换
     *
     * @param newTab 新的tab
     * @param oldTab 就得tab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (oldTab != null) {
            if (oldTab.fragment != null) {
                // 从界面隐藏,但是在Fragment的缓存空间中
                ft.hide(oldTab.fragment);
            }
        }

        if (newTab.fragment == null) {
            Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
            // 缓存Fragment
            newTab.fragment = fragment;
            // 提交到FragmentManager中
            ft.add(containerId, fragment, newTab.clx.getName());
        } else {
            // 从FragmentManager缓存中重新加载到界面中
            ft.show(newTab.fragment);
        }
        ft.commit();

        // 通知回调
        notifyTabSelect(newTab, oldTab);
    }

    /**
     * 执行点击回调监听器
     *
     * @param newTab 新的tab
     * @param oldTab 旧得tab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChanged(newTab, oldTab);
        }
    }

    private void notifyTabReselect(Tab<T> tab) {
        // TODO 二次点击Tab的操作
    }

    /**
     * 所有的tab基础属性
     *
     * @param <T> 泛型的额外的参数
     */
    public static class Tab<T> {
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        public Class<?> clx;
        /**
         * 额外的字段,用户自定义设定需要什么东西
         */
        public T extra;
        /**
         * 内部缓存对应的Fragment,Package权限,外部无法使用
         */
        Fragment fragment;
    }

    /**
     * 定义事件处理完成后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        /**
         * 在Tab切换时触发
         *
         * @param newTab 新的Tab(即点击的tab)
         * @param oldTab 旧的Tab
         */
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
