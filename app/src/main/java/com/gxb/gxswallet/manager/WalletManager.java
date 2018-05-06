package com.gxb.gxswallet.manager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.utils.SharedPreferenceUtils;

import java.util.List;

/**
 * 钱包管理器
 *
 * @author inrush
 * @date 2018/4/15.
 */

public class WalletManager {
    @SuppressLint("StaticFieldLeak")
    private static WalletManager mWalletManager;
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
     *
     * @param context {@link Application}持有全局的Application上下文,不能是其他的上下文,避免内存泄漏
     */
    public void init(Application context) {
        mContext = context;
        mWallets = new ArrayMap<>();
        mWalletDataManager = new WalletDataManager(mContext);
        mWalletDataList = mWalletDataManager.queryAll();
        if (mWalletDataList.size() == 0) {
            return;
        }
        for (WalletData wallet : mWalletDataList) {
            mWallets.put(wallet.getName(), wallet);
        }
        setCurrentWallet(getDefaultWallet());
    }

    public WalletData getWallet(String name) {
        return mWallets.get(name);
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
     * @param name
     * @param passwordPubKey
     * @param encryptionKey
     * @param encryptedWifkey
     * @param isBackUp
     * @param brainKey
     * @return
     */
    public boolean saveWallet( @NonNull String name,
                              @NonNull String passwordPubKey, @NonNull String encryptionKey,
                              @NonNull String encryptedWifkey, boolean isBackUp, String brainKey) {
        WalletData wallet = new WalletData(null, name, passwordPubKey, encryptionKey, encryptedWifkey, isBackUp, brainKey);
        return saveWallet(wallet);
    }

    public boolean saveWallet(WalletData wallet) {
        mWallets.put(wallet.getName(), wallet);
        mWalletDataList.add(wallet);
        return mWalletDataManager.insert(wallet);
    }

    public boolean updateWallet(WalletData wallet) {
        if (mWalletDataManager.update(wallet)) {
            mWallets.put(wallet.getName(), wallet);
            for (int i = 0; i < mWalletDataList.size(); i++) {
                if (mWalletDataList.get(i).getName().equals(wallet.getName())) {
                    mWalletDataList.remove(i);
                    mWalletDataList.add(i, wallet);
                }
            }
            if (mCurrentWallet != null && mCurrentWallet.getName().equals(wallet.getName())) {
                mCurrentWallet = wallet;
            }
            return true;
        }
        return false;
    }

    /**
     * 删除钱包
     *
     * @param wallet 需要删除的钱包
     * @return 是否删除成功
     */
    public boolean deleteWallet(WalletData wallet) {
        if (mWalletDataManager.delete(wallet)) {
            mWallets.remove(wallet.getName());
            for (int i = 0; i < mWalletDataList.size(); i++) {
                if (mWalletDataList.get(i).getName().equals(wallet.getName())) {
                    mWalletDataList.remove(i);
                    break;
                }
            }
            return true;
        }
        return false;
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
        if (mCurrentWallet == null) {
            return mWalletDataList.get(0);
        }
        return mCurrentWallet;
    }

    /**
     * 设置默认钱包
     *
     * @param wallet 需要设置的默认钱包
     */
    public void setDefaultWallet(WalletData wallet) {
        SharedPreferenceUtils.putString(mContext, SP_DEFAULT_WALLET, wallet.getName());
    }

    /**
     * 获取默认的钱包
     *
     * @return 默认钱包
     */
    public WalletData getDefaultWallet() {
        // 钱包是空的,返回空的默认钱包
        if (mWallets.size() == 0) {
            return null;
        }
        String defaultName = SharedPreferenceUtils.getString(mContext, SP_DEFAULT_WALLET, null);
        if (defaultName == null || mWallets.get(defaultName) == null) {
            setDefaultWallet(mWallets.valueAt(0));
            return mWallets.valueAt(0);
        }
        return mWallets.get(defaultName);
    }

}
