package com.sxds.common.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * @author inrush
 */
public class DensityHelper {

    private static int windowWidth;
    private static int windowHeight;

    /**
     * 根据手机的分辨率将dp的单位转成px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率将px(像素)的单位转成dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 屏幕宽度（像素）
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Activity context) {
        if (windowWidth == 0) {
            DisplayMetrics metric = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metric);
            windowWidth = metric.widthPixels;
        }
        return windowWidth;
    }

    /**
     * 屏幕高度（像素）
     *
     * @param activity
     * @return
     */
    public static int getWindowHeight(Activity activity) {
        if (windowHeight == 0) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            windowHeight = metric.heightPixels;
        }
        return windowHeight;
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);

        return rectangle.top;
    }
}
