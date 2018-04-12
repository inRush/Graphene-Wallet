package com.gxb.gxswallet.db.wallet;

import android.content.Context;

import com.gxb.gxswallet.db.BaseDaoManager;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/18.
 */

public class WalletDataManager extends BaseDaoManager<WalletData, Long> {

    public WalletDataManager(Context context) {
        super(context);
    }

    @Override
    protected AbstractDao<WalletData, Long> getDao() {
        return mManager.getDaoSession().getWalletDataDao();
    }

}
