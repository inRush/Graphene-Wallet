/*
 * Copyright 2013, 2014 Megion Research & Development GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gxb.sdk.crypto.ecc;


import com.gxb.sdk.crypto.utils.HexUtils;

import java.math.BigInteger;

public class Parameters {
   public static final Curve curve;
   public static final byte[] seed;
   public static final Point G;
   public static final BigInteger n;
   public static final BigInteger h;
   /**
    * The maximum number a signature can have in version 3 transactions
    */
   public static final BigInteger MAX_SIG_S;

   static {
      BigInteger p = new BigInteger(1,
            HexUtils.toBytes("fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f"));
      BigInteger a = BigInteger.ZERO;
      BigInteger b = BigInteger.valueOf(7);
      curve = new Curve(p, a, b);
      seed = null;
      G = curve.decodePoint(HexUtils.toBytes("04" + "79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798"
            + "483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8"));
      n = new BigInteger(1, HexUtils.toBytes("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141"));
      h = BigInteger.ONE;
      MAX_SIG_S = new BigInteger(1, HexUtils.toBytes("7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF5D576E7357A4501DDFE92F46681B20A0")); 
   }
}
