package com.gxb.sdk.crypto.ecc;

import com.google.common.primitives.Bytes;
import com.gxb.sdk.crypto.utils.Base58;
import com.gxb.sdk.crypto.utils.HashUtils;

import org.bouncycastle.math.ec.ECPoint;


/**
 * 公钥
 * @author inrush
 * @date 2018/3/10.
 */

public class PublicKey {
    private static final String ADDRESS_PREFIX = "GXC";
    private ECPoint Q;

    private PublicKey(ECPoint point) {
        this.Q = point;
    }

    public byte[] toBytes(boolean isCompressed) {
        if (this.Q == null) {
            return isCompressed ? new byte[33] : new byte[65];
        }
        return this.Q.getEncoded(isCompressed);
    }

    public String toPublicKeyString(String addressPrefix, boolean isCompressed) {
        byte[] pubBuf = this.toBytes(isCompressed);
        byte[] checksumData = HashUtils.addressHash(pubBuf);
        byte[] checksum = new byte[4];
        System.arraycopy(checksumData, 0, checksum, 0, 4);
        byte[] addr = Bytes.concat(pubBuf, checksum);
        return addressPrefix + Base58.encode(addr);
    }

    public String toPublicKeyString(boolean isCompressed) {
        return toPublicKeyString(ADDRESS_PREFIX, isCompressed);
    }

    public String toPublicKeyString() {
        return toPublicKeyString(true);
    }

    public static PublicKey fromPoint(ECPoint point) {
        return new PublicKey(point);
    }
}
