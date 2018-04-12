package com.gxb.gxswallet.db;

import android.content.Context;

import com.ping.greendao.gen.WalletDataDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/21.
 */

public abstract class BaseDaoManager<D, K> {
    protected DaoManager mManager;

    /**
     * 获取Dao
     *
     * @return
     */
    protected abstract AbstractDao<D, K> getDao();

    public BaseDaoManager(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    public boolean insert(D data) {
        boolean flag;
        flag = getDao().insert(data) != -1;
        return flag;
    }
    /**
     * 插入多条数据，在子线程操作
     *
     * @return
     */
    public boolean insertMult(final List<D> list) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(() -> {
                for (D data : list) {
                    getDao().insert(data);
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     *
     * @return
     */
    public boolean update(D data) {
        boolean flag = false;
        try {
            getDao().update(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @return
     */
    public boolean delete(D data) {
        boolean flag = false;
        try {
            //按照id删除
            getDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            getDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<D> queryAll() {
        return getDao().loadAll();
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public D queryById(K key) {
        return getDao().load(key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<D> queryByNativeSql(String sql, String[] conditions) {
        return getDao().queryRaw(sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<D> queryByQueryBuilder(long id) {
        QueryBuilder<D> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(WalletDataDao.Properties.Id.eq(id)).list();
    }
}
