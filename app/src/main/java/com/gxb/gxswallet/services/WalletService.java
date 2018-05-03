package com.gxb.gxswallet.services;

import com.google.gson.Gson;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.common.Task;
import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.db.asset.AssetData;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.db.asset.AssetSymbol;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.services.rpc.RpcTask;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.gxb.gxswallet.utils.AssetsUtil;
import com.gxb.sdk.api.GxbApis;
import com.gxb.sdk.bitlib.util.HexUtils;
import com.gxb.sdk.crypto.ecc.PrivateKey;
import com.gxb.sdk.crypto.utils.AesKeyCipher;
import com.gxb.sdk.crypto.utils.Base58;
import com.gxb.sdk.crypto.utils.BitUtils;
import com.gxb.sdk.crypto.utils.HashUtils;
import com.gxb.sdk.crypto.utils.KeyCipher;
import com.gxb.sdk.crypto.utils.KeyUtil;
import com.gxb.sdk.http.GxbCallBack;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cy.agorise.graphenej.Address;
import cy.agorise.graphenej.AssetAmount;
import cy.agorise.graphenej.BaseOperation;
import cy.agorise.graphenej.BlockData;
import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.PublicKey;
import cy.agorise.graphenej.Transaction;
import cy.agorise.graphenej.UserAccount;
import cy.agorise.graphenej.Util;
import cy.agorise.graphenej.api.GetAccountBalances;
import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.api.GetKeyReferences;
import cy.agorise.graphenej.api.GetNetworkDynamicParameters;
import cy.agorise.graphenej.api.GetObjects;
import cy.agorise.graphenej.api.GetRequiredFees;
import cy.agorise.graphenej.api.TransactionBroadcastSequence;
import cy.agorise.graphenej.api.android.WebSocketService;
import cy.agorise.graphenej.errors.ChecksumException;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.DynamicGlobalProperties;
import cy.agorise.graphenej.models.WitnessResponse;
import cy.agorise.graphenej.objects.Memo;
import cy.agorise.graphenej.operations.TransferOperation;
import cy.agorise.graphenej.operations.TransferOperationBuilder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import com.gxb.sdk.ecc.PrivateKey;
//import com.gxb.sdk.crypto.utils.AesKeyCipher;
//import com.gxb.sdk.crypto.utils.HexUtils;
//import com.gxb.sdk.crypto.utils.KeyUtil;

/**
 * @author inrush
 * @date 2018/3/11.
 */

public class WalletService {
    private static final String TAG = "WalletService";
    private Gson sGson;
    private static WalletService sWalletService;
    private WalletDataManager mWalletDataManager;
    private static final byte DEFAULT_VERSION = (byte) 0x80;


    private WalletService() {
        sGson = new Gson();
        mWalletDataManager = new WalletDataManager(App.getInstance());
    }


    public static WalletService getInstance() {
        if (sWalletService == null) {
            synchronized (WalletService.class) {
                if (sWalletService == null) {
                    sWalletService = new WalletService();
                }
            }
        }
        return sWalletService;
    }


    private boolean checkKeyAvailable(PublicKey pubKey, UserAccount account) {
        long weightThreshold = account.getActive().getWeightThreshold();
        HashMap<PublicKey, Long> keyAuths = account.getActive().getKeyAuths();
        return keyAuths.containsKey(pubKey) && keyAuths.get(pubKey) == weightThreshold;
    }

    private boolean checkIsExist(List<WalletData> wallets, WalletData wallet) {
        for (WalletData w : wallets) {
            if (w.getName().equals(wallet.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 服务监听器
     *
     * @param <D>
     */
    public interface ServerListener<D> {
        /**
         * 失败回调
         *
         * @param error 错误信息
         */
        void onFailure(Error error);

        /**
         * 成功回调
         *
         * @param data 获取的数据
         */
        void onSuccess(D data);
    }


    public void checkAddressIsExist(ECKey privateKey, ServerListener<Boolean> listener) {
        List<AssetData> assetDataList = AssetDataManager.getEnableList();
        for (AssetData assetData : assetDataList) {

        }
    }

    /**
     * 导入账户
     *
     * @param wifKey
     * @param password
     * @param listener
     * @throws IllegalArgumentException
     */
    public void importAccount(String wifKey, String password, final ServerListener<List<WalletData>> listener)
            throws IllegalArgumentException {
        WalletData walletData = lockWallet(null, "", "", wifKey, password, true, null);
        ECKey privateKey = DumpedPrivateKey.fromBase58(null, wifKey).getKey();
        List<Address> addresses = new ArrayList<>();
        AssetData asset = AssetDataManager.getDefault();
        addresses.add(new Address(ECKey.fromPublicOnly(privateKey.getPubKey()), asset.getPrefix()));
        RpcTask rpcTask = new RpcTask(
                new GetKeyReferences(WebSocketServicePool.getInstance().getService(asset.getName()), addresses),
                AssetSymbol.BTS.getName());
        rpcTask.run()
                .flatMap(rpcTask1 -> {
                    if (rpcTask1.getData() != null) {
                        List<List<UserAccount>> accounts = (List<List<UserAccount>>) rpcTask1.getData().result;
                        if (accounts.size() == 0 || accounts.get(0).size() == 0) {
                            return Observable.error(new Throwable(App.getInstance().getString(R.string.account_not_exist)));
                        }
                        List<String> ids = new ArrayList<>();
                        ids.add(accounts.get(0).get(0).getObjectId());
                        RpcTask task = new RpcTask(
                                new GetObjects(
                                        WebSocketServicePool.getInstance().getService(asset.getName()), ids), asset.getName());
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
                        List<WalletData> wallets = WalletManager.getInstance().getAllWallet();
                        List<WalletData> imported = new ArrayList<>();
                        String exist = "";
                        UserAccount account = ((List<UserAccount>) rpcTask.getData().result).get(0);
                        walletData.setName(account.getName());
                        walletData.setAccountId(account.getObjectId());
                        boolean isKeyAvailable = checkKeyAvailable(addresses.get(0).getPublicKey(), account);
                        if (isKeyAvailable) {
                            boolean alreadyExist = checkIsExist(wallets, walletData);
                            if (!alreadyExist) {
                                WalletManager.getInstance().saveWallet(walletData);
                                imported.add(walletData);
                            } else {
                                exist = walletData.getName();
                            }
                        }
                        if (!"".equals(exist)) {
                            listener.onFailure(new Error(App.getInstance().getString(R.string.wallet_already_exist, exist)));
                        } else {
                            listener.onSuccess(imported);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure(new Error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void createAccount(String walletName, String password, ServerListener<WalletData> listener) {
        String dict = AssetsUtil.getString("brainkeydict.txt");
        if (dict != null) {
            String suggestKey = BrainKey.suggest(dict);
            BrainKey brainKey = new BrainKey(suggestKey, BrainKey.DEFAULT_SEQUENCE_NUMBER);
            String wifKey = getWifKey(brainKey);
            WalletData wallet = lockWallet(null, "", walletName, wifKey, password, false, brainKey.getBrainKey());
            String pubKey = PrivateKey.fromWif(wifKey).toPublicKey().toPublicKeyString();

            GxbCallBack callBack = new GxbCallBack() {
                @Override
                public void onFailure(Error error) {
                    listener.onFailure(error);
                }

                @Override
                public void onSuccess(String result) {
                    String pubKey = PrivateKey.fromWif(wifKey).toPublicKey().toPublicKeyString("BTS");
                    GxbApis.getInstance()
                            .walletApi().registerAccount(new Object[]{
                            wallet.getName(), pubKey, pubKey, "nathan", "nathan", 0, true
                    }, new GxbCallBack() {
                        @Override
                        public void onFailure(Error error) {
                            listener.onFailure(new Error(error.getMessage()));
                        }

                        @Override
                        public void onSuccess(String result) {
                            new GetAccountByName(WebSocketServicePool.getInstance().getService(AssetSymbol.GXS.TEST), walletName).call(new WitnessResponseListener() {
                                @Override
                                public void onSuccess(WitnessResponse response) {
                                    AccountProperties account = (AccountProperties) response.result;
                                    wallet.setAccountId(account.id);
                                    WalletManager.getInstance().saveWallet(wallet);
                                    listener.onSuccess(wallet);
                                }

                                @Override
                                public void onError(BaseResponse.Error error) {
                                    listener.onFailure(new Error(error.message));
                                }
                            });
                        }
                    });

                }
            };
            GxbApis.getInstance()
                    .walletApi().registerAccount2(new Object[]{
                    wallet.getName(), pubKey, pubKey, pubKey, "1.2.28", "1.2.28", 10, "GXS", true
            }, callBack);
        }
    }

    public String[] unlockWallet(WalletData wallet, String password) {
        String[] results = new String[]{
                null, null
        };
        String passwordPub = PrivateKey.fromSeed(password).toPublicKey().toPublicKeyString();
        if (!passwordPub.equals(wallet.getPasswordPubKey())) {
            return results;
        }
        try {
            AesKeyCipher passwordAes = new AesKeyCipher(password);
            byte[] encryptionBytes = passwordAes.decrypt(HexUtils.toBytes(wallet.getEncryptionKey()));
            AesKeyCipher localAesPrivate = new AesKeyCipher(HexUtils.toHex(encryptionBytes));
            byte[] wifKeyByte = HexUtils.toBytes(wallet.getEncryptedWifkey());
            results[0] = new String(localAesPrivate.decrypt(wifKeyByte));
            if (!"".equals(wallet.getBrainKey()) && wallet.getBrainKey() != null) {
                byte[] brainKey = HexUtils.toBytes(wallet.getBrainKey());
                results[1] = new String(localAesPrivate.decrypt(brainKey));
            }
        } catch (KeyCipher.InvalidKeyCipher invalidKeyCipher) {
            invalidKeyCipher.printStackTrace();
        }
        return results;
    }

    /**
     * 根据brainKey 生成 wifKey
     * 采用SHA512和SHA256对称加密生成
     * 具体参考{@link BrainKey}构造函数
     *
     * @param brainKey 助记词
     * @return wifKey
     */
    public String getWifKey(BrainKey brainKey) {
        byte[] wifKeyBuffer = brainKey.getPrivateKey().getPrivKeyBytes();
        byte[] checkSumWifKeyBuffer = new byte[wifKeyBuffer.length + 5];
        checkSumWifKeyBuffer[0] = (byte) 0x80;
        byte[] tempBuffer = new byte[wifKeyBuffer.length + 1];
        System.arraycopy(wifKeyBuffer, 0, checkSumWifKeyBuffer, 1, wifKeyBuffer.length);
        System.arraycopy(checkSumWifKeyBuffer, 0, tempBuffer, 0, tempBuffer.length);
        byte[] checkSumBuffer = HashUtils.sha256(tempBuffer).getBytes();
        checkSumBuffer = HashUtils.sha256(checkSumBuffer).getBytes();
        byte[] checkSum = new byte[4];
        System.arraycopy(checkSumBuffer, 0, checkSum, 0, 4);
        System.arraycopy(checkSum, 0, checkSumWifKeyBuffer, wifKeyBuffer.length + 1, 4);
        return Base58.encode(checkSumWifKeyBuffer);
    }

    public byte[] getWifKeyBytes(String wifKey) {
        byte[] privateWif = Base58.decode(wifKey);
        assert privateWif != null;
        byte version = privateWif[0];
        if (version != DEFAULT_VERSION) {
            throw new IllegalArgumentException("Expected version" + 0x80 + ", instead got " + version);
        }
        byte[] privateKey = new byte[33];
        System.arraycopy(privateWif, 0, privateKey, 0, 33);
        byte[] checksum = new byte[4];
        System.arraycopy(privateWif, 33, checksum, 0, 4);
        byte[] newChecksum = HashUtils.sha256(privateKey).getBytes();
        newChecksum = HashUtils.sha256(newChecksum).getBytes();
        byte[] cs = new byte[4];
        System.arraycopy(newChecksum, 0, cs, 0, 4);
        boolean isEqual = BitUtils.areEqual(checksum, cs);
        if (!isEqual) {
            throw new IllegalArgumentException("Checksum did not match");
        }
        byte[] _privateKey = new byte[32];
        System.arraycopy(privateKey, 1, _privateKey, 0, 32);
        return _privateKey;
    }

    /**
     * 锁定钱包
     *
     * @param accountId
     * @param walletName
     * @param wifKey
     * @param password
     * @param isBackup
     * @param brainKey
     * @return
     */
    public WalletData lockWallet(Long id,
                                 String accountId, String walletName, String wifKey, String password, boolean isBackup, String brainKey) {
        AesKeyCipher passwordAes = new AesKeyCipher(password);
        // 获取一个随机生成的私钥
        byte[] encryptionBytes = KeyUtil.getRandomKey().toBytes();
        // 密码生成的加密密钥
        final String encryptionKey = HexUtils.toHex(passwordAes.encrypt(encryptionBytes));
        // 本地Aes加密器
        AesKeyCipher localAesPrivate = new AesKeyCipher(HexUtils.toHex(encryptionBytes));
        // 加密wifkey
        String encryptedWifkey = HexUtils.toHex(localAesPrivate.encrypt(wifKey.getBytes()));
        String encryptedBrainKey = brainKey;
        if (!"".equals(brainKey) && brainKey != null) {
            encryptedBrainKey = HexUtils.toHex(localAesPrivate.encrypt(brainKey.getBytes()));
        }
        PrivateKey passwordPrivate = PrivateKey.fromSeed(password);
        final String passwordPub = passwordPrivate.toPublicKey().toPublicKeyString();

        return new WalletData(id, accountId, walletName, passwordPub, encryptionKey, encryptedWifkey, isBackup, encryptedBrainKey);
    }

    public void lockWallet(WalletData wallet, String wifKey, String password) {
        AesKeyCipher passwordAes = new AesKeyCipher(password);
        // 获取一个随机生成的私钥
        byte[] encryptionBytes = KeyUtil.getRandomKey().toBytes();
        // 密码生成的加密密钥
        final String encryptionKey = HexUtils.toHex(passwordAes.encrypt(encryptionBytes));
        // 本地Aes加密器
        AesKeyCipher localAesPrivate = new AesKeyCipher(HexUtils.toHex(encryptionBytes));
        // 加密wifkey
        String encryptedWifkey = HexUtils.toHex(localAesPrivate.encrypt(wifKey.getBytes()));
        String encryptedBrainKey = wallet.getBrainKey();
        if (!"".equals(wallet.getBrainKey()) && wallet.getBrainKey() != null) {
            encryptedBrainKey = HexUtils.toHex(localAesPrivate.encrypt(wallet.getBrainKey().getBytes()));
        }
        PrivateKey passwordPrivate = PrivateKey.fromSeed(password);
        final String passwordPub = passwordPrivate.toPublicKey().toPublicKeyString();
        wallet.setPasswordPubKey(passwordPub);
        wallet.setEncryptionKey(encryptionKey);
        wallet.setEncryptedWifkey(encryptedWifkey);
        wallet.setBrainKey(encryptedBrainKey);
    }

    public Observable<HashMap<String, Double>> fetchAllAccountBalance(String accountName) {
        HashMap<String, Double> result = new HashMap<>(AssetDataManager.getEnableList().size());
        return Observable.create(e -> fetchAllAccountByName(accountName)
                .flatMap(data -> {
                    List<AssetData> coins = AssetDataManager.getEnableList();
                    List<Object[]> args = new ArrayList<>();
                    for (AssetData assetData : coins) {
                        Object[] arg = new Object[2];
                        arg[0] = data.get(assetData.getName()).id;
                        arg[1] = assetData.getAssetId();
                        args.add(arg);
                    }
                    RpcTask[] task = WebSocketServicePool.getInstance()
                            .generateTasks(GetAccountBalances.class, new Class[]{String.class, String.class}, args);
                    return Observable.fromArray(task).flatMap(Task::run);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rpcTask -> {
                    if (rpcTask.getState() == Task.State.COMPLETE) {
                        if (rpcTask.getData().result instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<AssetAmount> assetAmounts =
                                    (List<AssetAmount>) rpcTask.getData().result;
                            result.put(rpcTask.getTag(), assetAmounts.get(0).getAmount().doubleValue() / AssetDataManager.AMOUNT_SIZE);
                        }
                    }
                }, e::onError, () -> {
                    e.onNext(result);
                    e.onComplete();
                }));
    }

    public Observable<Double> fetchAccountBalance(WebSocketService service, String accountName, AssetData asset) {
        return Observable.create(e -> new RpcTask(new GetAccountByName(service, accountName), "")
                .run().flatMap(rpcTask -> {
                    AccountProperties account = (AccountProperties) rpcTask.getData().result;
                    return new RpcTask(new GetAccountBalances(service, account.id, asset.getAssetId()), "").run();
                }).subscribeOn(Schedulers.io())
                .subscribe(rpcTask -> {
                    List<AssetAmount> assetAmounts = (List<AssetAmount>) rpcTask.getData().result;
                    e.onNext(assetAmounts.get(0).getAmount().doubleValue() / AssetDataManager.AMOUNT_SIZE);
                }, e::onError, e::onComplete));

    }

    public Observable<HashMap<String, AccountProperties>> fetchAllAccountByName(String accountName) {
        RpcTask[] rpcTasks = WebSocketServicePool.getInstance().generateTasks(
                GetAccountByName.class, new Class[]{String.class}, new Object[]{accountName}
        );
        HashMap<String, AccountProperties> result = new HashMap<>();
        return Observable.create(e -> Observable.fromArray(rpcTasks)
                .flatMap(Task::run)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rpcTask -> {
                    if (rpcTask.getState() == Task.State.COMPLETE) {
                        if (rpcTask.getData().result instanceof AccountProperties) {
                            AccountProperties account = (AccountProperties) rpcTask.getData().result;
                            result.put(rpcTask.getTag(), account);
                        }
                    }
                }, e::onError, () -> {
                    e.onNext(result);
                    e.onComplete();
                }));
    }

    public Observable<HashMap<String, AccountProperties>> fetchListAccountByName(WebSocketService service, List<String> accountNames) {
        return Observable.create(e -> {
            RpcTask[] rpcTasks = new RpcTask[accountNames.size()];
            for (int i = 0; i < accountNames.size(); i++) {
                rpcTasks[i] = new RpcTask(new GetAccountByName(service, accountNames.get(i)), accountNames.get(i));
            }
            HashMap<String, AccountProperties> accountMap = new HashMap<>();
            Observable.fromArray(rpcTasks)
                    .flatMap(Task::run)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(rpcTask -> {
                        if (rpcTask.getData().result instanceof AccountProperties) {
                            AccountProperties account = (AccountProperties) rpcTask.getData().result;
                            accountMap.put(rpcTask.getTag(), account);
                        }
                    }, e::onError, () -> {
                        e.onNext(accountMap);
                        e.onComplete();
                    });
        });
    }

    public Observable<TransferOperation>
    generateTransferOperation(WebSocketService service, ECKey sourcePrivate,
                              String fromName, String toName, AssetAmount amount, String memo) {
        return Observable.create(e -> {
            List<String> accounts = new ArrayList<>();
            accounts.add(fromName);
            accounts.add(toName);

            fetchListAccountByName(service, accounts)
                    .subscribe(accounts1 -> {
                        Memo m = null;
                        if (memo != null && !"".equals(memo)) {
                            m = generateMemo(sourcePrivate, AssetDataManager.get(service.getSocketTag()),
                                    accounts1.get(fromName).options.getMemoKey(), accounts1.get(toName).options.getMemoKey(), memo);
                        }
                        TransferOperation transferOperation = new TransferOperationBuilder()
                                .setTransferAmount(amount)
                                .setSource(new UserAccount(accounts1.get(fromName).id))
                                .setDestination(new UserAccount(accounts1.get(toName).id))
                                .setMemo(m)
                                .build();
                        e.onNext(transferOperation);
                    }, e::onError, e::onComplete);
        });
    }

    public Memo generateMemo(ECKey sourcePrivate, AssetData assetData, PublicKey fromPub, PublicKey toPub, String memo) {
        Address from = new Address(fromPub.getKey(), assetData.getPrefix());
        Address to = new Address(toPub.getKey(), assetData.getPrefix());
        BigInteger nonce = BigInteger.ONE;
        byte[] encryptedMessage = Memo.encryptMessage(sourcePrivate, to, nonce, memo);
        return new Memo(from, to, nonce, encryptedMessage);
    }

    public String decryptMemo(ECKey privateKey, Memo memo, boolean isSend) throws Exception {
        try {
            return Memo.decryptMessage(privateKey, isSend ? memo.getDestination() : memo.getSource(), memo.getNonce(), memo.getByteMessage());
        } catch (ChecksumException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Observable<Boolean> transfer(WebSocketService service, ECKey sourcePrivate, ArrayList<BaseOperation> transferOperations, AssetData feesAmount) {
        return Observable.create(e -> {
            final ArrayList<BaseOperation> operationList = new ArrayList<>();
            operationList.addAll(transferOperations);
            Transaction transaction = new Transaction(
                    Util.hexToBytes(service.getChainId()), sourcePrivate, null, operationList);
            new RpcTask(new GetRequiredFees(service, feesAmount.getAssetId(), operationList), "")
                    .run()
                    .flatMap(rpcTask -> {
                        List<AssetAmount> assetAmounts = (List<AssetAmount>) rpcTask.getData().result;
                        transaction.setFees(assetAmounts);
                        return new RpcTask(new GetNetworkDynamicParameters(service), "").run();
                    })
                    .flatMap(rpcTask -> {
                        DynamicGlobalProperties properties = (DynamicGlobalProperties) rpcTask.getData().result;
                        long expirationTime = (properties.time.getTime() / 1000) + Transaction.DEFAULT_EXPIRATION_TIME;
                        String headBlockId = properties.head_block_id;
                        long headBlockNumber = properties.head_block_number;
                        transaction.setBlockData(new BlockData(headBlockNumber, headBlockId, expirationTime));
                        return new RpcTask(new TransactionBroadcastSequence(service, transaction), "")
                                .run();
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rpcTask -> e.onNext(true), e::onError, e::onComplete);
        });
    }
}
