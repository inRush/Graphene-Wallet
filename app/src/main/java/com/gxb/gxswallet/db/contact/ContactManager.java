package com.gxb.gxswallet.db.contact;

import android.content.Context;

import com.gxb.gxswallet.db.BaseDaoManager;
import com.ping.greendao.gen.ContactDataDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class ContactManager extends BaseDaoManager<ContactData, Long> {
    public ContactManager(Context context) {
        super(context);
    }

    @Override
    protected AbstractDao<ContactData, Long> getDao() {
        return mManager.getDaoSession().getContactDataDao();
    }

    public boolean isExist(ContactData contact) {
        QueryBuilder<ContactData> queryBuilder = getDao().queryBuilder();
        return queryBuilder.where(ContactDataDao.Properties.Address.eq(contact.getAddress())).count() > 0;
    }
}
