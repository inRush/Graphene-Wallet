package com.gxb.sdk.crypto.utils;


import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Secp256k1 {

    static {
		Crypt.init();
	}

	private final static ECParameterSpec p = ECNamedCurveTable.getParameterSpec("secp256k1");
	private final static ECCurve curve = p.getCurve();
	private final static ECPoint G = p.getG();

	public static ECPoint getPoint(BigInteger k) {
		return G.multiply(k.mod(p.getN()));
	}

	public static ECPoint getG() {
		return G;
	}

	public static BigInteger getN() {
		return p.getN();
	}

	public static int getFieldSize() {
		return p.getCurve().getFieldSize();
	}

	public static ECCurve getCurve() {
		return curve;
	}

}