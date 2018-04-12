package com.sxds.common.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.Window;

import com.sxds.common.R;


/**
 * 解决弹出时顶部状态栏变黑问题
 * @author inrush
 * @date 2017/8/7.
 * @package me.inrush.common.widget
 */

public class AnimationBottomSheetDialog extends BottomSheetDialog {
    public AnimationBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public AnimationBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected AnimationBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        if (window == null) {
            return;
        }

        window.setWindowAnimations(R.style.DialogBottom);
    }
}
