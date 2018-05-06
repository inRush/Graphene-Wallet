package com.gxb.gxswallet.page.addcontact;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.asset.AssetSymbol;
import com.gxb.gxswallet.db.contact.ContactData;
import com.gxb.gxswallet.db.contact.ContactDataManager;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.sxds.common.presenter.BasePresenter;

import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.WitnessResponse;

/**
 * @author inrush
 * @date 2018/3/23.
 */

public class AddContactPresenter extends BasePresenter<AddContactContract.View>
        implements AddContactContract.Presenter {

    private ContactDataManager mContactDataManager;
    private static final int CHECK_EXIST_LOADING_CODE = 0x11;

    AddContactPresenter(AddContactContract.View view) {
        super(view);
        mContactDataManager = new ContactDataManager(App.getInstance());
    }

    @Override
    public boolean saveContact(ContactData contact) {
        return !mContactDataManager.isExist(contact) && mContactDataManager.insert(contact);
    }

    @Override
    public void checkWalletExist(String name) {
        getView().showLoading(CHECK_EXIST_LOADING_CODE, App.getInstance().getString(R.string.check_wallet_name));
        new GetAccountByName(WebSocketServicePool.getInstance().getService(AssetSymbol.GXS.TEST),name).call(new WitnessResponseListener() {
            @Override
            public void onSuccess(WitnessResponse response) {
                getView().dismissLoading(CHECK_EXIST_LOADING_CODE);
                if (response.result == null) {
                    getView().onCheckWalletExistSuccess(false, name);
                } else {
                    getView().onCheckWalletExistSuccess(true, name);
                }
            }

            @Override
            public void onError(BaseResponse.Error error) {
                getView().dismissLoading(CHECK_EXIST_LOADING_CODE);
                getView().onCheckWalletExistError(new Error(error.message));
            }
        });
    }
}
