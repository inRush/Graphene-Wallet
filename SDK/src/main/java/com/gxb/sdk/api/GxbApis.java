package com.gxb.sdk.api;

import com.gxb.sdk.api.baas.BaasApi;
import com.gxb.sdk.api.baas.BaasApiImpl;
import com.gxb.sdk.api.block.BlockApi;
import com.gxb.sdk.api.block.BlockApiImpl;
import com.gxb.sdk.api.wallet.WalletApi;
import com.gxb.sdk.api.wallet.WalletApiImpl;

/**
 * @author inrush
 * @date 2018/2/14.
 */

public class GxbApis {
    private static GxbApis mApis;
    private BaasApi mBaasApi;
    private WalletApi mWalletApi;
    private BlockApi mBlockApi;

    private GxbApis() {

    }

    public static GxbApis getInstance() {
        if (mApis == null) {
            synchronized (GxbApis.class) {
                if (mApis == null) {
                    mApis = new GxbApis();
                }
            }
        }
        return mApis;
    }

    public BaasApi baasApi() {
        if (mBaasApi == null) {
            synchronized (GxbApis.class) {
                if (mBaasApi == null) {
                    mBaasApi = new BaasApiImpl();
                }
            }
        }
        return mBaasApi;
    }

    /**
     * 钱包Api
     *
     * @return WalletApi
     */
    public WalletApi walletApi() {
        if (mWalletApi == null) {
            synchronized (GxbApis.class) {
                if (mWalletApi == null) {
                    mWalletApi = new WalletApiImpl();
                }
            }
        }
        return mWalletApi;
    }

    /**
     * 区块Api
     *
     * @return BlockApi
     */
    public BlockApi blockApi() {
        if (mBlockApi == null) {
            synchronized (GxbApis.class) {
                if (mBlockApi == null) {
                    mBlockApi = new BlockApiImpl();
                }
            }
        }
        return mBlockApi;
    }
}
