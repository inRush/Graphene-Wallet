package com.gxb.gxswallet.page.addcontact;

import com.gxb.gxswallet.db.contact.ContactData;
import com.sxds.common.presenter.BaseContract;

/**
 * @author inrush
 * @date 2018/3/23.
 */

class AddContactContract {
    interface View extends BaseContract.View<Presenter> {
        void onCheckWalletExistSuccess(boolean isExist,String walletName);
        void onCheckWalletExistError(Error error);

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 保存联系人
         *
         * @param contact
         */
        boolean saveContact(ContactData contact);

        void checkWalletExist(String name);
    }
}
