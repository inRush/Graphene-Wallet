package com.gxb.gxswallet.page.editcontact;

import com.gxb.gxswallet.db.contact.ContactData;
import com.sxds.common.presenter.BaseContract;

/**
 * @author inrush
 * @date 2018/3/23.
 */

class EditContactContract {
    interface View extends BaseContract.View<EditContactContract.Presenter> {

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 保存联系人
         *
         * @param contact 联系人
         * @return boolean
         */
        boolean updateContact(ContactData contact);
    }
}
