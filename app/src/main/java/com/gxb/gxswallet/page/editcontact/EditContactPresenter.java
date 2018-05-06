package com.gxb.gxswallet.page.editcontact;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.db.contact.ContactDataManager;
import com.sxds.common.presenter.BasePresenter;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class EditContactPresenter extends BasePresenter<EditContactContract.View>
        implements EditContactContract.Presenter {
    private ContactDataManager mContactDataManager;

    public EditContactPresenter(EditContactContract.View view) {
        super(view);
        mContactDataManager = new ContactDataManager(App.getInstance());
    }



    @Override
    public boolean updateContact(ContactData contact) {
        return mContactDataManager.update(contact);
    }
}
