package com.gxb.gxswallet.page.send;

import com.google.common.primitives.UnsignedLong;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.gxswallet.services.rpc.RpcTask;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.gxb.gxswallet.utils.CodeUtil;
import com.sxds.common.presenter.BasePresenter;

import org.bitcoinj.core.ECKey;

import java.util.ArrayList;
import java.util.List;

import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.api.GetRequiredFees;
import cy.agorise.graphenej.api.android.WebSocketService;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author inrush
 * @date 2018/3/16.
 */

public class SendPresenter extends BasePresenter<SendContract.View>
        implements SendContract.Presenter {

    public SendPresenter(SendActivity view) {
        super(view);
    }

    @Override
    public void send(ECKey privateKey, String from, String to, String amount, AssetData assetData, String memo) {
        WebSocketService service = WebSocketServicePool.getInstance().getService(assetData.getName());
        ArrayList<BaseOperation> operations = new ArrayList<>();
        getView().showLoading(CodeUtil.getCode(), R.string.query_fee);
        WalletService.getInstance().generateTransferOperation(service, privateKey, from, to,
                new AssetAmount(UnsignedLong.valueOf(amount), new Asset(assetData.getAssetId())), memo)
                .flatMap(transferOperation -> {
                    operations.add(transferOperation);
                    return Observable.create((ObservableOnSubscribe<Boolean>) e -> new RpcTask(new GetRequiredFees(service, assetData.getAssetId(), operations), "")
                            .run()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(rpcTask -> {
                                List<AssetAmount> assetAmounts = (List<AssetAmount>) rpcTask.getData().result;
                                getView().onQueryFeeSuccess(assetAmounts.get(0).getAmount().doubleValue() / AssetDataManager.AMOUNT_SIZE,
                                        aBoolean -> {
                                            e.onNext(aBoolean);
                                            if (aBoolean) {
                                                getView().showLoading(CodeUtil.getCode(), R.string.sending);
                                            } else {
                                                getView().showInfo(R.string.transfer_cancel);
                                            }
                                            return null;
                                        });
                            }, e::onError));
                })
                .flatMap(aBoolean -> {
                    if (aBoolean) {
                        return WalletService.getInstance().transfer(service, privateKey, operations, assetData);
                    } else {
                        return Observable.empty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> getView().onSendSuccess(), error -> getView().showError(error.getMessage()));
    }


    @Override
    public List<WalletData> fetchWallet() {
        return WalletManager.getInstance().getAllWallet();
    }

    @Override
    public List<AssetData> fetchAssets() {
        return AssetDataManager.getEnableList();
    }


    @Override
    public void fetchWalletBalance(WalletData wallet, AssetData asset) {
        WebSocketService service = WebSocketServicePool.getInstance().getService(asset.getName());
        WalletService.getInstance().fetchAccountBalance(service, wallet.getName(), asset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> getView().onFetchWalletBalanceSuccess(wallet, result),
                        error -> getView().showError(error.getMessage()));
    }

}
