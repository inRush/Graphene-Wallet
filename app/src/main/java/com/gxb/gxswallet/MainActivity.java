package com.gxb.gxswallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gxb.sdk.api.GxbApis;
import com.gxb.sdk.http.GxbCallBack;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

public class MainActivity extends AppCompatActivity {

    private QMUITopBar mTopBar;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_main);
        mTopBar = findViewById(R.id.topbar);
        mTopBar.setTitle("GXB");
        test();
//        byte[] private_wif = Base58.decode("5KQiXpeTfxvBe5AB4Q2ZdkhwPxTdyj4Y2abdk5W1qerRGoptMer");
//        byte[] private_key = new byte[private_wif.length - 4];
//        byte[] checksum = new byte[4];
//        for (int i = 0, j = 0; i < private_wif.length; i++) {
//            if (i < private_wif.length - 4) {
//                private_key[i] = private_wif[i];
//            } else {
//                checksum[j++] = private_wif[i];
//            }
//        }
//
//        String new_checksum = EncryptUtil.sha256(checksum);
//
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//            // curveName这里取值：secp256k1
//            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
//            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            // 获取公钥
//            keyPair.getPublic();
//            // 获取私钥
//            keyPair.getPrivate();
//
//        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG, "onCreate: " + new_checksum);
    }

    public void test() {
        GxbApis.getInstance().walletApi().lookupAssetSymbols("GXS", new GxbCallBack() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(String result) {
                Logger.json(result);
            }
        });
    }
}
