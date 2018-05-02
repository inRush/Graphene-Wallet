package com.gxb.gxswallet.db.asset;

import com.gxb.gxswallet.BuildConfig;

/**
 * 支持的资产符号管理类
 *
 * @author inrush
 * @date 2018/3/23.
 */

public class AssetSymbol {

    public static class GXS {
        public static final String TEST = "GXS";
        public static final String PRODUCT = "GXS";
        public static String getName() {
            if (BuildConfig.DEBUG) {
                return TEST;
            }
            return PRODUCT;
        }
    }

    public static class BTS {
        public static final String TEST = "TEST";
        public static final String PRODUCT = "BTS";

        public static String getName() {
            if (BuildConfig.DEBUG) {
                return TEST;
            }
            return PRODUCT;
        }
    }
}
