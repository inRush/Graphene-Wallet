package com.gxb.gxswallet.utils;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class CodeUtil {
    private static int code = 0x100;

    public static int getCode() {
        return code++;
    }
}
