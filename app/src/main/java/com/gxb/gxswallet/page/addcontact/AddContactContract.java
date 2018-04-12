package com.gxb.gxswallet.page.addcontact;

import com.gxb.gxswallet.db.contact.ContactData;
import com.sxds.common.presenter.BaseContract;

/**
 * @author inrush
 * @date 2018/3/23.
 */

class AddContactContract {
    interface View extends BaseContract.View<Presenter> {

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 保存联系人
         *
         * @param contact
         */
        boolean saveContact(ContactData contact);
    }
}
