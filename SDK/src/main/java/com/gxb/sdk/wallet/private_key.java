package com.gxb.sdk.wallet;


import com.gxb.sdk.bitlib.crypto.HmacPRNG;
import com.gxb.sdk.bitlib.crypto.InMemoryPrivateKey;
import com.gxb.sdk.bitlib.crypto.RandomSource;
import com.gxb.sdk.bitlib.crypto.SignedMessage;
import com.gxb.sdk.bitlib.crypto.ec.EcTools;
import com.gxb.sdk.bitlib.crypto.ec.Parameters;
import com.gxb.sdk.bitlib.crypto.ec.Point;
import com.gxb.sdk.bitlib.util.Sha256Hash;
import com.gxb.sdk.wallet.fc.crypto.sha256_object;
import com.gxb.sdk.wallet.fc.crypto.sha512_object;
import com.gxb.sdk.wallet.graphene.chain.compact_signature;

import org.bitcoinj.core.ECKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;


public class private_key {
    private byte[] key_data = new byte[32];
    public private_key(byte key[]) {
        System.arraycopy(key, 0, key_data, 0, key_data.length);
    }

    public byte[] get_secret() {
        return key_data;
    }

    public static private_key generate() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDsA", "SC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
            keyGen.initialize(ecSpec, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            return new private_key(keyPair);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return null;
    }

    public public_key get_public_key() {
        try {
            ECNamedCurveParameterSpec secp256k1 = ECNamedCurveTable.getParameterSpec("secp256k1");
            ECPrivateKeySpec privSpec = new ECPrivateKeySpec(new BigInteger(1, key_data), secp256k1);
            KeyFactory keyFactory = KeyFactory.getInstance("EC","SC");

            byte[] keyBytes = new byte[33];
            System.arraycopy(key_data, 0, keyBytes, 1, 32);
            BigInteger privateKeys = new BigInteger(keyBytes);
            BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(privSpec);

            Point Q = EcTools.multiply(Parameters.G, privateKeys);

            //ECPoint ecPoint = ECKey.CURVE.getG().multiply(privateKeys);
            ECPoint ecpubPoint = new SecP256K1Curve().createPoint(Q.getX().toBigInteger(), Q.getY().toBigInteger());
            PublicKey publicKey = keyFactory.generatePublic(new ECPublicKeySpec(ecpubPoint, secp256k1));

            BCECPublicKey bcecPublicKey = (BCECPublicKey)publicKey;
            byte bytePublic[] = bcecPublicKey.getQ().getEncoded(true);

            return new public_key(bytePublic);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    private private_key(KeyPair ecKey){
        BCECPrivateKey privateKey = (BCECPrivateKey) ecKey.getPrivate();
        byte[] privateKeyGenerate = privateKey.getD().toByteArray();
        if (privateKeyGenerate.length == 33) {
            System.arraycopy(privateKeyGenerate, 1, key_data, 0, key_data.length);
        } else {
            System.arraycopy(privateKeyGenerate, 0, key_data, 0, key_data.length);
        }
    }

    public compact_signature sign_compact(sha256_object digest, boolean require_canonical ) {
        compact_signature signature = null;
        try {
            final HmacPRNG prng = new HmacPRNG(key_data);
            RandomSource randomSource = new RandomSource() {
                @Override
                public void nextBytes(byte[] bytes) {
                    prng.nextBytes(bytes);
                }
            };

            while (true) {
                InMemoryPrivateKey inMemoryPrivateKey = new InMemoryPrivateKey(key_data);
                SignedMessage signedMessage = inMemoryPrivateKey.signHash(new Sha256Hash(digest.hash), randomSource);
                byte[] byteCompact = signedMessage.bitcoinEncodingOfSignature();
                signature = new compact_signature(byteCompact);

                boolean bResult = public_key.is_canonical(signature);
                if (bResult) {
                    break;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return signature;
    }

    public static private_key from_seed(String strSeed) {
        sha256_object.encoder encoder = new sha256_object.encoder();

        encoder.write(strSeed.getBytes(Charset.forName("UTF-8")));
        private_key privateKey = new private_key(encoder.result().hash);

        return privateKey;
    }

    public sha512_object get_shared_secret(public_key publicKey) {
        ECKey ecPublicKey = ECKey.fromPublicOnly(publicKey.getKeyByte());
        ECKey ecPrivateKey = ECKey.fromPrivate(key_data);

        byte[] secret = ecPublicKey.getPubKeyPoint().multiply(ecPrivateKey.getPrivKey())
                .normalize().getXCoord().getEncoded();

        return sha512_object.create_from_byte_array(secret, 0, secret.length);
    }

}
