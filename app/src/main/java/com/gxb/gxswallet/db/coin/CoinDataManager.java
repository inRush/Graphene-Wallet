package com.gxb.gxswallet.db.coin;

import android.content.Context;

import com.gxb.gxswallet.db.BaseDaoManager;
import com.ping.greendao.gen.CoinDataDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class CoinDataManager extends BaseDaoManager<CoinData, Long> {

    public CoinDataManager(Context context) {
        super(context);
    }

    @Override
    protected AbstractDao<CoinData, Long> getDao() {
        return mManager.getDaoSession().getCoinDataDao();
    }

    public List<CoinData> queryEnableCoin() {
        QueryBuilder<CoinData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(CoinDataDao.Properties.Enable.eq(true)).list();
    }

}
