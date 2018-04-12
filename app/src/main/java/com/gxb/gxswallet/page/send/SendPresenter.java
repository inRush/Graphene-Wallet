package com.gxb.gxswallet.page.send;

import com.google.common.primitives.UnsignedLong;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.sdk.models.chain.CurrentChainParam;
import com.gxb.sdk.models.wallet.AccountBalance;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.orhanobut.logger.Logger;
import com.sxds.common.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;

import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.TransactionBroadcastSequence;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.WitnessResponse;
import cy.agorise.graphenej.objects.Memo;
import cy.agorise.graphenej.operations.TransferOperation;
import cy.agorise.graphenej.operations.TransferOperationBuilder;
import cy.agorise.graphenej.test.NaiveSSLContext;


/**
 * @author inrush
 * @date 2018/3/16.
 */

public class SendPresenter extends BasePresenter<SendContract.View>
        implements SendContract.Presenter {

    private WalletDataManager mWalletDataManager;

    public SendPresenter(SendActivity view) {
        super(view);
        mWalletDataManager = new WalletDataManager(App.getInstance());
    }

    @Override
    public void send(String from, String to, String amount, String memo) {

        try {
//             Creating a transfer operation
            TransferOperation transferOperation = new TransferOperationBuilder()
                    .setTransferAmount(new AssetAmount(UnsignedLong.valueOf(10000), new Asset("1.3.1")))
                    .setSource(new UserAccount("1.2.33"))
                    .setDestination(new UserAccount("1.2.28"))
                    .setFee(new AssetAmount(UnsignedLong.valueOf(2000), new Asset("1.3.1")))
                    .setMemo(new Memo())
                    .build();

            // Adding operations to the operation list
            final ArrayList<BaseOperation> operationList = new ArrayList<>();
            operationList.add(transferOperation);

//            DumpedPrivateKey key = DumpedPrivateKey.fromBase58(null,"5KQiXpeTfxvBe5AB4Q2ZdkhwPxTdyj4Y2abdk5W1qerRGoptMer");
//            key.getKey().getPubKey();

            String wif = "5J1rd3naNYCYeuxJVoFRjQuwZuDdyNZP3NZXZqiLD1cavZZTnym";
            ECKey sourcePrivate = DumpedPrivateKey.fromBase58(null, wif).getKey();
            Address address = new Address(ECKey.fromPublicOnly(sourcePrivate.getPubKey()), "GXC");

            Transaction transaction = new Transaction(sourcePrivate,null, operationList);
            SSLContext context = null;
            try {
                context = NaiveSSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            WebSocketFactory factory = new WebSocketFactory();
            factory.setSSLContext(context);

            try {
                WebSocket mWebSocket;
                mWebSocket = factory.createSocket("ws://106.14.180.117:28090");
                mWebSocket.addListener(new TransactionBroadcastSequence(transaction, new Asset("1.3.1"),
                        new WitnessResponseListener() {
                            @Override
                            public void onSuccess(WitnessResponse response) {
                                Logger.e((String) response.result);
                            }

                            @Override
                            public void onError(BaseResponse.Error error) {
                                Logger.e(error.message);
                            }
                        }));
//                List<Asset> assets = new ArrayList<>();
//                assets.add(new Asset("1.3.1"));
//                mWebSocket.addListener(new GetAccountBalances(new UserAccount("1.2.28"), assets,
//                        new WitnessResponseListener() {
//                            @Override
//                            public void onSuccess(WitnessResponse response) {
//                                List<AssetAmount> assetAmounts = (List<AssetAmount>) response.result;
//                                for (AssetAmount assetAmount : assetAmounts) {
//                                    Logger.e(assetAmount.toJsonString());
//                                }
//                            }
//
//                            @Override
//                            public void onError(BaseResponse.Error error) {
//                                Logger.e(error.message);
//                            }
//                        }));
                final WebSocket finalMWebSocket = mWebSocket;
                Run.onBackground(new Action() {
                    @Override
                    public void call() {
//                        try {
//                            finalMWebSocket.connect();
//                        } catch (WebSocketException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
////            BitsharesWalletWraper.getInstance().import_key("test-inrush1","128911hwj",
////                    "5J1rd3naNYCYeuxJVoFRjQuwZuDdyNZP3NZXZqiLD1cavZZTnym");
////            BitsharesWalletWraper.getInstance().unlock("128911hwj");
//            BitsharesWalletWraper.getInstance().transfer("1.2.33","1.2.28","0.1","1.3.1",null);
//        } catch (NetworkStatusException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public List<WalletData> fetchWallet() {
        return mWalletDataManager.queryAll();
    }


    private long baseExpirationSec(String headBlockSecStr) {
        headBlockSecStr = headBlockSecStr.replace('T', ' ');
        //2018-03-28T07:19:24
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        // 设置格林威治时区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long headBlockSec = sdf.parse(headBlockSecStr).getTime() / 1000;
            long nowSec = System.currentTimeMillis() / 1000;
            if (nowSec - headBlockSec > 30) {
                return headBlockSec;
            }
            return Math.max(nowSec, headBlockSec);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private void sendTo(List<CurrentChainParam> params) {

    }

    @Override
    public void fetchWalletBalance(final WalletData wallet) {
        WalletService.getInstance()
                .fetchAccountBalance(wallet.getName(),
                        new WalletService.ServerListener<List<AccountBalance>>() {
                            @Override
                            public void onFailure(Error error) {

                            }

                            @Override
                            public void onSuccess(final List<AccountBalance> data) {
                                Run.onUiAsync(() -> getView().onFetchWalletBalanceSuccess(wallet, data));
                            }
                        });
    }

}
