package com.gxb.sdk.crypto.ecc;

import com.google.common.primitives.Bytes;
import com.gxb.sdk.crypto.utils.Base58;
import com.gxb.sdk.crypto.utils.BitUtils;
import com.gxb.sdk.crypto.utils.HashUtils;
import com.gxb.sdk.crypto.utils.Secp256k1;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * 私钥
 *
 * @author inrush
 * @date 2018/3/10.
 */

public class PrivateKey {
    /**
     * 转换成私钥的 byte 数组长度
     */
    private static final int BYTES_ARRAY_LENGTH = 32;
    private static final byte DEFAULT_VERSION = (byte) 0x80;

    private BigInteger _d;
    private PublicKey _publicKey;

    private PrivateKey(BigInteger d) {
        _d = d;
    }

    /**
     * 根据bytes数组获取PrivateKey
     *
     * @param bytes Base58 解析出来的数组
     * @return {@link #PrivateKey(BigInteger)}
     */
    public static PrivateKey fromBytes(byte[] bytes) {
        if (bytes.length != BYTES_ARRAY_LENGTH) {
            throw new IllegalArgumentException("WARN: Expecting 32 bytes, instead got " + bytes.length);
        }
        byte[] newBytes = Bytes.concat(new byte[1], bytes);
        return new PrivateKey(new BigInteger(newBytes));
    }


    public static PrivateKey fromWif(String wifKey) throws IllegalArgumentException {
        byte[] privateWif = Base58.decode(wifKey);
        assert privateWif != null;
        byte version = privateWif[0];
        if (version != DEFAULT_VERSION) {
            throw new IllegalArgumentException("Expected version" + 0x80 + ", instead got " + version);
        }
        byte[] privateKey = new byte[33];
        System.arraycopy(privateWif, 0, privateKey, 0, 33);
        byte[] checksum = new byte[4];
        System.arraycopy(privateWif, 33, checksum, 0, 4);
        byte[] newChecksum = HashUtils.sha256(privateKey).getBytes();
        newChecksum = HashUtils.sha256(newChecksum).getBytes();
        byte[] cs = new byte[4];
        System.arraycopy(newChecksum, 0, cs, 0, 4);
        boolean isEqual = BitUtils.areEqual(checksum, cs);
        if (!isEqual) {
            throw new IllegalArgumentException("Checksum did not match");
        }
        byte[] _privateKey = new byte[32];
        System.arraycopy(privateKey, 1, _privateKey, 0, 32);

        return PrivateKey.fromBytes(_privateKey);
    }

    public static PrivateKey fromSeed(String seed) {
        return PrivateKey.fromBytes(HashUtils.sha256(seed.getBytes()).getBytes());
    }

    private ECPoint toPublicKeyPoint() {
        return Secp256k1.getG().multiply(this._d);
    }

    public PublicKey toPublicKey() {
        if (_publicKey == null) {
            this._publicKey = PublicKey.fromPoint(toPublicKeyPoint());
        }
        return _publicKey;
    }

    public byte[] toBytes() {
        return this._d.toByteArray();
    }

    @Override
    public String toString() {
        return this._d.toString();
    }
}
