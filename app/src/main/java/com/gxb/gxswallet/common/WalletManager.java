package com.gxb.gxswallet.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;

import java.util.List;

/**
 * 钱包管理器
 *
 * @author inrush
 * @date 2018/4/15.
 */

public class WalletManager {
    private static WalletManager mWalletManager;
    private static final String SP_NAME = "GXB";
    private static final String SP_DEFAULT_WALLET = "default_wallet";
    private Context mContext;
    /**
     * 所有钱包
     */
    private ArrayMap<String, WalletData> mWallets;
    private List<WalletData> mWalletDataList;
    /**
     * 当前的钱包
     */
    private WalletData mCurrentWallet;
    private WalletDataManager mWalletDataManager;


    public static WalletManager getInstance() {
        if (mWalletManager == null) {
            synchronized (WalletManager.class) {
                if (mWalletManager == null) {
                    mWalletManager = new WalletManager();
                }
            }
        }
        return mWalletManager;
    }

    private WalletManager() {

    }

    /**
     * 初始化本地的所有钱包
     */
    public void init(Context context) {
        mContext = context;
        mWallets = new ArrayMap<>();
        mWalletDataManager = new WalletDataManager(mContext);
        mWalletDataList = mWalletDataManager.queryAll();
        for (WalletData wallet : mWalletDataList) {
            mWallets.put(wallet.getName(), wallet);
        }
        if (mWallets.size() == 0) {
            return;
        }
        setCurrentWallet(getDefaultWallet());
    }

    public WalletData getWallet(String address) {
        return mWallets.get(address);
    }

    /**
     * 获取所有的钱包
     *
     * @return 钱包列表
     */
    public List<WalletData> getAllWallet() {
        return mWalletDataList;
    }

    /**
     * 保存钱包
     *
     * @param accountId
     * @param name
     * @param passwordPubKey
     * @param encryptionKey
     * @param encryptedWifkey
     * @param isBackUp
     * @param brainKey
     * @return
     */
    public boolean saveWallet(@NonNull String accountId, @NonNull String name,
                              @NonNull String passwordPubKey, @NonNull String encryptionKey,
                              @NonNull String encryptedWifkey, boolean isBackUp, String brainKey) {
        WalletData wallet = new WalletData(null, accountId, name, passwordPubKey, encryptionKey, encryptedWifkey, isBackUp, brainKey);
        return saveWallet(wallet);
    }

    public boolean saveWallet(WalletData wallet) {
        mWallets.put(wallet.getName(), wallet);
        mWalletDataList.add(wallet);
        return mWalletDataManager.insert(wallet);
    }

    /**
     * 删除钱包
     *
     * @param wallet 需要删除的钱包
     * @return 是否删除成功
     */
    public boolean deleteWallet(WalletData wallet) {
        return mWalletDataManager.delete(wallet);
    }

    /**
     * 获取本地钱包的数目
     *
     * @return 钱包的数量
     */
    public int getCount() {
        return mWallets.size();
    }


    /**
     * 设置当前显示的钱包
     */
    public void setCurrentWallet(WalletData wallet) {
        mCurrentWallet = wallet;
    }

    /**
     * 获取当前的钱包
     *
     * @return {@link WalletData} 当前的钱包
     */
    public WalletData getCurrentWallet() {
        return mCurrentWallet;
    }

    /**
     * 设置默认钱包
     *
     * @param wallet 需要设置的默认钱包
     */
    public void setDefaultWallet(WalletData wallet) {
        SharedPreferences preferences = mContext.getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_DEFAULT_WALLET, wallet.getName());
        editor.apply();
    }

    /**
     * 获取默认的钱包
     *
     * @return 默认钱包
     */
    public WalletData getDefaultWallet() {
        SharedPreferences preferences = mContext.getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        String defaultName = preferences.getString(SP_DEFAULT_WALLET, null);
        if (defaultName == null || mWallets.get(defaultName) == null) {
            setDefaultWallet(mWallets.valueAt(0));
            return mWallets.valueAt(0);
        }
        return mWallets.get(defaultName);
    }

}
