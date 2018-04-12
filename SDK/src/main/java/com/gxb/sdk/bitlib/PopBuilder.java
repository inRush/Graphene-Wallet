package com.gxb.sdk.bitlib;


import com.gxb.sdk.bitlib.crypto.IPublicKeyRing;
import com.gxb.sdk.bitlib.model.NetworkParameters;
import com.gxb.sdk.bitlib.model.TransactionOutput;
import com.gxb.sdk.bitlib.model.UnspentTransactionOutput;

import java.util.List;

public class PopBuilder extends StandardTransactionBuilder {
    public PopBuilder(NetworkParameters network) {
        super(network);
    }

    private static class UnsignedPop extends UnsignedTransaction {
        public static final int MAX_LOCK_TIME = 499999999;
        private static final int POP_SEQUENCE_NUMBER = 0;

        private UnsignedPop(List<TransactionOutput> outputs, List<UnspentTransactionOutput> funding, IPublicKeyRing keyRing, NetworkParameters network) {
            super(outputs, funding, keyRing, network);
        }

        @Override
        public int getDefaultSequenceNumber() {
            return POP_SEQUENCE_NUMBER;
        }

        @Override
        public int getLockTime() {
            return MAX_LOCK_TIME;
        }
    }

    public UnsignedPop createUnsignedPop(List<TransactionOutput> outputs, List<UnspentTransactionOutput> funding,
                                         IPublicKeyRing keyRing, NetworkParameters network) {
        UnsignedPop unsignedPop = new UnsignedPop(outputs, funding, keyRing, network);

       return unsignedPop;
    }
}
