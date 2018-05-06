package com.gxb.gxswallet.page.contacts;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.db.contact.ContactDataManager;
import com.sxds.common.presenter.BasePresenter;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class ContactsPresenter extends BasePresenter<ContactsContract.View>
        implements ContactsContract.Presenter {

    private ContactDataManager mContactDataManager;

    ContactsPresenter(ContactsContract.View view) {
        super(view);
        mContactDataManager = new ContactDataManager(App.getInstance());
    }

    @Override
    public List<ContactData> fetchContacts() {
        return mContactDataManager.queryAll();
    }
}
