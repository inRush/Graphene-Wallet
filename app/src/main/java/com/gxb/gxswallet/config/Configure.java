package com.gxb.gxswallet.config;

/**
 * @author inrush
 * @date 2018/3/17.
 */

public class Configure {
    public static final String QR_CODE_PRE_FIX = "qr://transfer?";

    public static class GXS {
        public static class TEST {
            public static final String ID = "1.3.1";
            public static final String NAME = "GXS";
            public static final String[] NETS = new String[]{
                    "ws://106.14.180.117:28090"
            };
        }

        public static class PRODUCT {
            public static final String ID = "1.3.1";
            public static final String NAME = "GXS";
            public static final String[] NETS = new String[]{
                    "wss://node1.gxb.io",
                    "wss://node5.gxb.io",
                    "wss://node8.gxb.io",
                    "wss://node11.gxb.io",
                    "wss://node15.gxb.io",
                    "wss://node16.gxb.io",
                    "wss://node17.gxb.io"
            };
        }
    }

    public static class BTS {
        public static class TEST {
            public static final String ID = "1.3.0";
            public static final String NAME = "TEST";
            public static final String[] NETS = new String[]{
                    "wss://node.testnet.bitshares.eu/ws"
            };
        }

        public static class PRODUCT {
            public static final String ID = "1.3.0";
            public static final String NAME = "BTS";
            public static final String[] NETS = new String[]{
                    "wss://bitshares.dacplay.org/ws"
            };
        }
    }
}
