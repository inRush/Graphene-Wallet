package com.gxb.gxswallet.db.asset;

import android.content.Context;

import com.gxb.gxswallet.db.BaseDaoManager;
import com.ping.greendao.gen.AssetDataDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public class AssetDataManager extends BaseDaoManager<AssetData, Long> {



    public AssetDataManager(Context context) {
        super(context);
    }

    @Override
    protected AbstractDao<AssetData, Long> getDao() {
        return mManager.getDaoSession().getAssetDataDao();
    }

    public List<AssetData> queryEnableAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.Enable.eq(true)).list();
    }

    public List<AssetData> queryTestAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(true)).list();
    }

    public List<AssetData> queryProductAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(false)).list();
    }

    public List<AssetData> queryEnableTestAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(true),
                AssetDataDao.Properties.Enable.eq(true)).list();
    }

    public List<AssetData> queryEnableProductAsset() {
        QueryBuilder<AssetData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(AssetDataDao.Properties.IsTest.eq(true),
                AssetDataDao.Properties.Enable.eq(true)).list();
    }


}
