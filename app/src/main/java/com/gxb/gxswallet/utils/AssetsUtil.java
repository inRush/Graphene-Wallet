package com.gxb.gxswallet.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gxb.gxswallet.App;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class AssetsUtil {
    private static final String PRE_FIX = "assets/";

    public static Bitmap getImage(String fileName) {
        InputStream is = App.getInstance()
                .getClass().getClassLoader()
                .getResourceAsStream(PRE_FIX + fileName);
        return BitmapFactory.decodeStream(is);
    }

    public static String getString(String fileName) {
        String str = null;
        try {
            InputStream is = App.getInstance().getAssets().open(fileName);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            str = new String(bytes, "utf-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
