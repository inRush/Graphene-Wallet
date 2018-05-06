package com.gxb.gxswallet.page.addcontact;

import com.gxb.gxswallet.db.contact.ContactData;
import com.sxds.common.presenter.BaseContract;

/**
 * @author inrush
 * @date 2018/3/23.
 */

class AddContactContract {
    interface View extends BaseContract.View<Presenter> {
        /**
         * 检查钱包是否存在的成功回调
         *
         * @param isExist       是否存在
         * @param walletAddress 钱包的地址
         */
        void onCheckWalletExistSuccess(boolean isExist, String walletAddress);

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 保存联系人
         *
         * @param contact 联系人
         * @return 是否保存成功
         */
        boolean saveContact(ContactData contact);

        /**
         * 检查钱包是否存在
         *
         * @param address 钱包的地址
         */
        void checkWalletExist(String address);
    }
}
