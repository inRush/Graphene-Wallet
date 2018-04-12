package com.sxds.common.helper;

/**
 * @author inrush
 * @date 2017/9/23.
 * @package com.sxds.oaplat.helper
 */

public class NumberHelper {

    private static int TEN = 10;
    /**
     * 获取一个数字的位数
     * @param num 数字
     * @return 位数
     */
    public static int getDigits(int num) {
        int digits = 1;
        while (num / TEN > 0) {
            digits++;
            num /= TEN;
        }
        return digits;
    }

    /**
     * 获取一个指定位数的整数 (1的倍数)
     * @param digits 位数
     * @return 数字
     */
    public static int getNumberWithDigits(int digits) {
        if (digits <= 0) {
            return 0;
        }
        int num = 1;
        for (int i = 1; i < digits; i++) {
            num *= 10;
        }
        return num;
    }

    /**
     * 取范围随机数[start,end)
     *
     * @param start 开始范围 (取的到)
     * @param end   结束范围 (取不到)
     * @return 随机数
     */
    public static int getRandomInteger(int start, int end) {
        if (start < end) {
            return -1;
        }
        int dis = end - start;
        double num = Math.random();
        return (int) (start + num * dis);
    }
}
