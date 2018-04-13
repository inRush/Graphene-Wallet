package com.gxb.gxswallet.page.test;

import android.widget.TextView;

import com.gxb.gxswallet.R;
import com.gxb.sdk.bitlib.util.HexUtils;
import com.sxds.common.app.BaseActivity;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;

import java.math.BigInteger;

import butterknife.BindView;
import cy.agorise.graphenej.PublicKey;
import cy.agorise.graphenej.errors.ChecksumException;
import cy.agorise.graphenej.objects.Memo;

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
        ECKey privateKey = DumpedPrivateKey.fromBase58(NetworkParameters.prodNet(), mWifKey).getKey();
        PublicKey publicKey = new PublicKey(ECKey.fromPublicOnly(privateKey.getPubKey()));
        String msg = "Public Key: " + publicKey.getAddress() + "\n";
        try {
            String message = Memo.decryptMessage(privateKey, publicKey, mNonce, mMessage);
        } catch (ChecksumException e) {
            e.printStackTrace();
        }

        mMessageTv.setText(msg);

    }
}
