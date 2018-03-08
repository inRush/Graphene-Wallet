package com.gxb.sdk.utils;

import java.security.SecureRandom;
import java.util.Date;

/**
 * @author inrush
 * @date 2018/2/18.
 */

public class KeyUtil {
    private static final int HASH_POWER_MILLS = 250;
    private static final int MIN_ENTROPY_LENGTH = 32;
    private static SecureRandom sr = new SecureRandom();

    private static String appEntropy() {
        String entropyStr = EncryptUtil.sha256((new Date()).toString());
        return entropyStr + (new Date()).toString();
    }

    private static String random32ByteBuffer(String entropy) {
        if (entropy.length() < MIN_ENTROPY_LENGTH) {
            throw new Error("expecting at least " + MIN_ENTROPY_LENGTH + " bytes of entropy");
        }

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < HASH_POWER_MILLS) {
            entropy = EncryptUtil.sha256(entropy);
        }
        return EncryptUtil.sha256(entropy.concat(sr.nextInt() + ""));
    }

//    public static String suggestBrainKey(String data){
//
//    }
}
