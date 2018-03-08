package com.gxb.sdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author inrush
 * @date 2018/2/18.
 */

public class EncryptUtil {


    public static String sha256(String data) {
        return sha(data, "SHA-256");
    }

    public static String sha512(String data) {
        return sha(data, "SHA-512");
    }

    public static String sha512(byte[] data) {
        return sha(data, "SHA-512");
    }

    public static String sha256(byte[] data) {
        return sha(data, "SHA-256");
    }

    /**
     * sha 加密
     *
     * @param data 数据
     * @return 加密数据
     */
    private static String sha(String data, String algorithm) {
        byte[] bt = data.getBytes();
        return sha(bt, algorithm);
    }

    private static String sha(byte[] data, String algorithm) {
        MessageDigest md;
        String strDes;
        try {
            // 将此换成SHA-1、SHA-512、SHA-384等参数
            md = MessageDigest.getInstance(algorithm);
            md.update(data);
            // to HexString
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    private static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }
}
