package com.gxb.gxswallet.page.addcontact;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.db.contact.ContactManager;
import com.sxds.common.presenter.BasePresenter;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class AddContactPresenter extends BasePresenter<AddContactContract.View>
        implements AddContactContract.Presenter {

    private ContactManager mContactManager;

    AddContactPresenter(AddContactContract.View view) {
        super(view);
        mContactManager = new ContactManager(App.getInstance());
    }

    @Override
    public boolean saveContact(ContactData contact) {
        if (mContactManager.isExist(contact)) {
            return false;
        }
        return mContactManager.insert(contact);
    }
}
