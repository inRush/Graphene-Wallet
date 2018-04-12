package com.gxb.gxswallet.page.contacts;

import com.gxb.gxswallet.db.contact.ContactData;
import com.sxds.common.presenter.BaseContract;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/23.
 */

class ContactsContract {
    interface View extends BaseContract.View<Presenter>{

    }

    interface Presenter extends BaseContract.Presenter{
        List<ContactData> fetchContacts();
    }
}
