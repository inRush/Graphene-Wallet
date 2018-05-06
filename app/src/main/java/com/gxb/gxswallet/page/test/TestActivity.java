package com.gxb.gxswallet.page.test;

import android.widget.TextView;

import com.google.common.primitives.UnsignedLong;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.Task;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetSymbol;
import com.gxb.gxswallet.manager.AssetManager;
import com.gxb.gxswallet.services.WalletService;
import com.gxb.gxswallet.services.rpc.RpcTask;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.gxb.gxswallet.utils.AssetsUtil;
import com.gxb.sdk.bitlib.util.HexUtils;
import com.gxb.sdk.crypto.ecc.PrivateKey;
import com.orhanobut.logger.Logger;
import com.sxds.common.app.BaseActivity;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.Asset;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.GrapheneObject;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.api.GetKeyReferences;
import cy.agorise.graphenej.api.GetObjects;
import cy.agorise.graphenej.api.GetRequiredFees;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.errors.MalformedAddressException;
import cy.agorise.graphenej.models.AccountProperties;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author inrush
 * @date 2018/4/12.
 */

public class TestActivity extends BaseActivity {

    @BindView(R.id.message)
    TextView mMessageTv;

    private String mWifKey = "5KQiXpeTfxvBe5AB4Q2ZdkhwPxTdyj4Y2abdk5W1qerRGoptMer";
    private String mPubKey = "GXC71dmKedXkypkqy2vXTaoMyuCoKKLnphixhQuHwKUZKTrvBz82c";
    private BigInteger mNonce = BigInteger.valueOf(5189668234366885422L);
    private byte[] mMessage = HexUtils.toBytes("1171ff1cfc4c788dd5ed1aa455849512");

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        AssetManager.getInstance().init(App.getInstance());
        WebSocketServicePool.getInstance().initPool(App.getInstance());
        findViewById(R.id.message).setOnClickListener(v -> {
            //        testMoreRequest();
//        testRequest();
//        testRxjava();
            testTransfer();
//            testGetObjects();
//            testMemo();
        });

//        testPrivateKey();
//        ECKey privateKey = DumpedPrivateKey.fromBase58(null, mWifKey).getKey();
////        String msg = "Public Key: " + publicKey.getAddress() + "\n";
//
//        try {
//            byte[] bytes = new Address(mPubKey).getPublicKey().getKey().getPubKeyPoint().multiply(privateKey.getPrivKey()).normalize().getXCoord().getEncoded();
//            bytes = HashUtils.sha512(bytes).getBytes();
//            String message = Memo.decryptMessage(privateKey, new Address(mPubKey), mNonce, mMessage);
//        } catch (ChecksumException | MalformedAddressException e) {
//            e.printStackTrace();
//        }

//        mMessageTv.setText(msg);


    }

    private void testMemo() {
        try {
            Address from = new Address("GXC5rWnyP42hd4WK5Vu54s3a91n6U2AnypeqDgwj895JKC2YfKf6B");
            Address to = new Address("GXC71dmKedXkypkqy2vXTaoMyuCoKKLnphixhQuHwKUZKTrvBz82c");
            BigInteger nonce = new BigInteger("5189668234366885422");
            ECKey sourcePrivate = DumpedPrivateKey.fromBase58(null, mWifKey).getKey();

//            Memo memo = WalletService.getInstance().generateMemo(sourcePrivate,from.getPublicKey(),to.getPublicKey(),"test");
        } catch (MalformedAddressException e) {
            e.printStackTrace();
        }
    }

    private void testGetObjects() {
//        new GetObjects(WebSocketServicePool.getInstance().getService(AssetData.getDefault().getName()),)
    }


    private void testPrivateKey() {
        String dict = AssetsUtil.getString("brainkeydict.txt");
        String suggestKey = BrainKey.suggest(dict);
        BrainKey brainKey = new BrainKey(suggestKey, BrainKey.DEFAULT_SEQUENCE_NUMBER);
        ECKey privateKey = brainKey.getPrivateKey();
        String wifKey = WalletService.getInstance().getWifKey(brainKey);
        ECKey pkey = ECKey.fromPrivate(WalletService.getInstance().getWifKeyBytes(wifKey));
        PrivateKey periKey = PrivateKey.fromWif(wifKey);
    }

    private void testTransfer() {
        AssetData assetData = AssetManager.getInstance().getDefault();
        WebSocketService service = WebSocketServicePool.getInstance().getService(assetData.getName());
        ECKey privateKey = DumpedPrivateKey.fromBase58(null, mWifKey).getKey();
        final BaseOperation[] operation = {null};
        WalletService.getInstance().generateTransferOperation(service, privateKey, "in-rush", "test-inrush1",
                new AssetAmount(UnsignedLong.valueOf(1000), new Asset(assetData.getAssetId())), "test")
                .flatMap(transferOperation -> {
                    List<BaseOperation> operations = new ArrayList<>();
                    operations.add(transferOperation);
                    operation[0] = transferOperation;
                    return new RpcTask(new GetRequiredFees(service, assetData.getAssetId(), operations), "").run();
                })
                .flatMap(rpcTask -> {
                    List<AssetAmount> assetAmounts = (List<AssetAmount>) rpcTask.getData().result;
                    if (assetAmounts == null || assetAmounts.size() == 0) {
                        return Observable.error(new Throwable("request error"));
                    }
                    return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
                        e.onNext(true);
                        e.onComplete();
                    });
                })
                .flatMap(aBoolean -> {
                    if (aBoolean) {
                        ArrayList<BaseOperation> operations = new ArrayList<>();
                        operations.add(operation[0]);
                        return WalletService.getInstance().transfer(service, privateKey, operations, assetData);
                    }
                    return Observable.empty();
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                }, error -> Logger.e(error.getMessage()), () -> {
                    Logger.e("onComplete");
                });
    }

    private void testRxjava() {
        Observable.fromArray(1, 2).flatMap(new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer integer) throws Exception {
                if (integer == 1) {
                    return Observable.empty();
                }
                return Observable.just(integer);
            }
        })
                .flatMap(new Function<Integer, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Integer integer) throws Exception {
                        Logger.e(integer.toString());
                        return Observable.just(integer);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Logger.e(o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Logger.e("complete");
                    }
                });
    }

    private void testRequest() {
        AssetManager.getInstance().init(App.getInstance());
        WebSocketServicePool.getInstance().initPool(App.getInstance());
        ECKey privateKey = DumpedPrivateKey.fromBase58(null, "5HzsQXxAGgTKNBtMiaPQEXMrLKRTGDNpboBjyUUfucaqLpV8Asq").getKey();
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(ECKey.fromPublicOnly(privateKey.getPubKey()), AssetSymbol.BTS.getName()));
        if (WebSocketServicePool.getInstance().checkAllServiceConnectSuccess()) {
            RpcTask[] rpcTasks = WebSocketServicePool.getInstance()
                    .generateTasks(GetAccountByName.class, new Class[]{String.class}, new Object[]{"in-rush"});

            Observable.fromArray(rpcTasks).flatMap(Task::run)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RpcTask>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(RpcTask task) {
//                                if (task.getTag().equals(AssetSymbol.GXS.getName())) {
                            Logger.e(((AccountProperties) task.getData().result).id);
//                                }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Logger.e("complete");
                        }
                    });
        }
    }

    private void testMoreRequest() {
        AssetManager.getInstance().init(App.getInstance());
        WebSocketServicePool.getInstance().initPool(App.getInstance());
        ECKey privateKey = DumpedPrivateKey.fromBase58(null, mWifKey).getKey();

        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(ECKey.fromPublicOnly(privateKey.getPubKey())));
        RpcTask rpcTask = new RpcTask(
                new GetKeyReferences(WebSocketServicePool.getInstance().getService(AssetSymbol.GXS.getName()), addresses),
                AssetSymbol.GXS.getName());
        rpcTask.run()
                .flatMap(rpcTask1 -> {
                    if (rpcTask1.getData() != null) {
                        List<List<UserAccount>> accounts = (List<List<UserAccount>>) rpcTask1.getData().result;
                        List<String> ids = new ArrayList<>();
                        ids.add(accounts.get(0).get(0).getObjectId());
                        RpcTask task = new RpcTask(
                                new GetObjects(
                                        WebSocketServicePool.getInstance().getService(AssetSymbol.GXS.getName()), ids),
                                AssetSymbol.GXS.getName());
                        return task.run();
                    } else {
                        return Observable.error(new Throwable("request is not available"));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RpcTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RpcTask rpcTask) {
                        Logger.e(
                                ((UserAccount) ((List<GrapheneObject>) rpcTask.getData().result).get(0)).toJsonString()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Logger.e("complete");
                    }
                });

    }
}
