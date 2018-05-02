package com.gxb.gxswallet.page.receive;

import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.sxds.common.presenter.BasePresenter;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/17.
 */

public class ReceivePresenter extends BasePresenter<ReceiveContract.View>
        implements ReceiveContract.Presenter {
    ReceivePresenter(ReceiveContract.View view) {
        super(view);
    }


    @Override
    public List<AssetData> fetchSupportCoin() {
        return AssetDataManager.getEnableList();
    }

    @Override
    public List<WalletData> fetchWallet() {
        return WalletManager.getInstance().getAllWallet();
    }
}
