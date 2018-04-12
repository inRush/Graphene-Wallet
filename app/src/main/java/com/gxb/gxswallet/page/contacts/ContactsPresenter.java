package com.gxb.gxswallet.page.contacts;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.db.contact.ContactManager;
import com.sxds.common.presenter.BasePresenter;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class ContactsPresenter extends BasePresenter<ContactsContract.View>
        implements ContactsContract.Presenter {

    private ContactManager mContactManager;

    ContactsPresenter(ContactsContract.View view) {
        super(view);
        mContactManager = new ContactManager(App.getInstance());
    }

    @Override
    public List<ContactData> fetchContacts() {
        return mContactManager.queryAll();
    }
}
