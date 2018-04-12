package com.gxb.gxswallet.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.coin.CoinData;
import com.gxb.gxswallet.db.coin.CoinDataManager;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.utils.AssetsUtil;
import com.gxb.gxswallet.utils.ListUtil;
import com.gxb.sdk.api.GxbApis;
import com.gxb.sdk.bitlib.util.HexUtils;
import com.gxb.sdk.crypto.ecc.PrivateKey;
import com.gxb.sdk.crypto.utils.AesKeyCipher;
import com.gxb.sdk.crypto.utils.Base58;
import com.gxb.sdk.crypto.utils.HashUtils;
import com.gxb.sdk.crypto.utils.KeyCipher;
import com.gxb.sdk.crypto.utils.KeyUtil;
import com.gxb.sdk.http.GxbCallBack;
import com.gxb.sdk.models.global.Account;
import com.gxb.sdk.models.wallet.AccountBalance;

import java.util.ArrayList;
import java.util.List;

import cy.agorise.graphenej.BrainKey;
import cy.agorise.graphenej.api.GetAccountByName;
import cy.agorise.graphenej.interfaces.WitnessResponseListener;
import cy.agorise.graphenej.models.AccountProperties;
import cy.agorise.graphenej.models.BaseResponse;
import cy.agorise.graphenej.models.WitnessResponse;

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


    private List<String> getAccountIds(String json) {
        JsonArray array = new JsonParser().parse(json).getAsJsonArray().get(0).getAsJsonArray();
        List<String> strings = new ArrayList<>();
        for (JsonElement je : array) {
            strings.add(new Gson().fromJson(je, String.class));
        }
        ListUtil.uniqList(strings);
        return strings;
    }

    private boolean checkKeyAvailable(String pubKey, Account account) {
        int weightThreshold = account.getActive().getWeight_threshold();
        List<List<String>> keyAuths = account.getActive().getKey_auths();
        for (List<String> key : keyAuths) {
            if (key.get(0).equals(pubKey) && Integer.parseInt(key.get(1)) == weightThreshold) {
                return true;
            }
        }
        return false;
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
        WalletData walletData = lockWallet("", "", wifKey, password, true, null);
        PrivateKey privateKey = PrivateKey.fromWif(wifKey);
        final String publicKey = privateKey.toPublicKey().toPublicKeyString();

        GxbApis.getInstance()
                .walletApi()
                .getKeyReferences(new Object[]{publicKey}, new GxbCallBack() {
                    @Override
                    public void onFailure(Error error) {
                        listener.onFailure(error);
                    }

                    @Override
                    public void onSuccess(String result) {
                        List<String> ids = getAccountIds(result);
                        Object[] params = new Object[ids.size()];
                        for (int i = 0; i < ids.size(); i++) {
                            params[i] = ids.get(i);
                        }
                        if (params.length == 0) {
                            listener.onFailure(new Error(App.getInstance().getString(R.string.account_not_exist)));
                        } else {
                            GxbApis.getInstance()
                                    .globalApi()
                                    .getObjects(params, new GxbCallBack() {
                                        @Override
                                        public void onFailure(Error error) {
                                            listener.onFailure(error);
                                        }

                                        @Override
                                        public void onSuccess(String result) {
                                            JsonArray accounts = new JsonParser().parse(result).getAsJsonArray();
                                            List<WalletData> wallets = mWalletDataManager.queryAll();
                                            List<WalletData> imported = new ArrayList<>();
                                            String exist = "";
                                            for (JsonElement a : accounts) {
                                                Account account = sGson.fromJson(a, Account.class);
                                                walletData.setName(account.getName());
                                                walletData.setAccountId(account.getId());
                                                boolean isKeyAvailable = checkKeyAvailable(publicKey, account);
                                                if (isKeyAvailable) {
                                                    boolean alreadyExist = checkIsExist(wallets, walletData);
                                                    if (!alreadyExist) {
                                                        mWalletDataManager.insert(walletData);
                                                        wallets.add(walletData);
                                                        imported.add(walletData);
                                                    } else {
                                                        exist = walletData.getName();
                                                    }
                                                }
                                            }
                                            if (!"".equals(exist)) {
                                                listener.onFailure(new Error(App.getInstance().getString(R.string.wallet_already_exist, exist)));
                                            } else {
                                                listener.onSuccess(imported);
                                            }
                                        }
                                    });
                        }

                    }
                });
    }

    public void createAccount(String walletName, String password, ServerListener<WalletData> listener) {
        String dict = AssetsUtil.getString("brainkeydict.txt");
        if (dict != null) {
            String suggestKey = BrainKey.suggest(dict);
            BrainKey brainKey = new BrainKey(suggestKey, BrainKey.DEFAULT_SEQUENCE_NUMBER);
            String wifKey = getWifKey(brainKey);
            WalletData wallet = lockWallet("", walletName, wifKey, password, false, brainKey.getBrainKey());
            String pubKey = PrivateKey.fromWif(wifKey).toPublicKey().toPublicKeyString();


            GxbApis.getInstance()
                    .walletApi().registerAccount2(new Object[]{
                    wallet.getName(), pubKey, pubKey, pubKey, "1.2.28", "1.2.28", 10, "GXS", true
            }, new GxbCallBack() {
                @Override
                public void onFailure(Error error) {
                    listener.onFailure(error);
                }

                @Override
                public void onSuccess(String result) {
                    new GetAccountByName(walletName).call(new WitnessResponseListener() {
                        @Override
                        public void onSuccess(WitnessResponse response) {
                            AccountProperties account = (AccountProperties) response.result;
                            wallet.setAccountId(account.id);
                            mWalletDataManager.insert(wallet);
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

    public WalletData lockWallet(String accountId, String walletName, String wifKey, String password, boolean isBackup, String brainKey) {
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

        return new WalletData(null, accountId, walletName, passwordPub, encryptionKey, encryptedWifkey, isBackup, encryptedBrainKey);
    }


    public void fetchAccountBalance(String accountName, final ServerListener<List<AccountBalance>> listener) {
        fetchAccountByName(accountName, new ServerListener<Account>() {
            @Override
            public void onFailure(Error error) {
                listener.onFailure(error);
            }

            @Override
            public void onSuccess(Account data) {
                List<CoinData> coins = new CoinDataManager(App.getInstance()).queryAll();
                Object[] assestIds = new Object[coins.size()];
                for (int i = 0; i < assestIds.length; i++) {
                    assestIds[i] = coins.get(i).getAssetId();
                }
                GxbApis.getInstance()
                        .walletApi()
                        .getAccountBalances(new Object[]{data.getId(), assestIds},
                                new GxbCallBack() {
                                    @Override
                                    public void onFailure(Error error) {
                                        listener.onFailure(error);
                                    }

                                    @Override
                                    public void onSuccess(String result) {
                                        List<AccountBalance> accountBalances = sGson.fromJson(result,
                                                new TypeToken<List<AccountBalance>>() {
                                                }.getType());
                                        listener.onSuccess(accountBalances);
                                    }
                                });
            }
        });
    }

    public void fetchAccountByName(String accountName, final ServerListener<Account> listener) {
        GxbApis.getInstance()
                .walletApi().getAccountByName(new Object[]{accountName}, new GxbCallBack() {
            @Override
            public void onFailure(Error error) {
                listener.onFailure(error);
            }

            @Override
            public void onSuccess(String result) {
                Account account = sGson.fromJson(result, Account.class);
                listener.onSuccess(account);
            }
        });
    }
}
