package com.gxb.gxswallet.page.importaccount;

import com.gxb.gxswallet.db.wallet.WalletData;
import com.sxds.common.presenter.BaseContract;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/13.
 */

class ImportWalletContract {
    interface View extends BaseContract.View<Presenter> {
        /**
         * 导入账户成功
         *
         * @param wallet
         */
        void onImportAccountSuccess(List<WalletData> wallet);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 导入账户
         *
         * @param wifKey   wifKey
         * @param password 密码
         */
        void importAccount(String wifKey, String password);
    }
}
