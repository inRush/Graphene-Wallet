package com.gxb.gxswallet.page.createwallet;

import com.gxb.gxswallet.db.wallet.WalletData;
import com.sxds.common.presenter.BaseContract;

/**
 * @author inrush
 * @date 2018/3/31.
 */

class CreateWalletContract {
    interface View extends BaseContract.View<Presenter> {
        /**
         * 检查钱包账户是否存在成功回调
         *
         * @param isExist 是否存在
         */
        void onCheckWalletExistSuccess(boolean isExist,String walletName);

        /**
         * 检查钱包账户是否存在失败回调
         *
         * @param error Error
         */
        void onCheckWalletExistError(Error error);

        void onWalletCreateSuccess(WalletData wallet);

    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 检查钱包是否存在了
         *
         * @param name Wallet name
         */
        void checkWalletExist(String name);

        /**
         * 检查钱包是否符合要求
         *
         * @param name 钱包的名称
         */
        String checkWalletName(String name);

        void createWallet(String walletName,String password);
    }
}
