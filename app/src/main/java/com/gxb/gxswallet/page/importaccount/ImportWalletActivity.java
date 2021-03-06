package com.gxb.gxswallet.page.importaccount;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.wallet.WalletData;
import com.gxb.gxswallet.page.main.MainActivity;
import com.gxb.gxswallet.services.WalletService;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.app.PresenterActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cy.agorise.graphenej.BrainKey;

/**
 * @author inrush
 * @date 2018/3/13.
 */

public class ImportWalletActivity extends PresenterActivity<ImportWalletContract.Presenter>
        implements ImportWalletContract.View {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;
    @BindView(R.id.memorizing_words_et)
    EditText memorizingWordsEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.again_input_et)
    EditText againPasswordEt;
    @BindView(R.id.type_group)
    RadioGroup typeGroup;

    private int type = 0;

    private int mloadingId = generateLoadingId();


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ImportWalletActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_import_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTopBar.setTitle(getString(R.string.import_wallet));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        typeGroup.check(R.id.brainKey);
        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.brainKey) {
                    type = 0;
                    memorizingWordsEt.setHint(R.string.input_memorizing_word_here);
                } else {
                    type = 1;
                    memorizingWordsEt.setHint(R.string.input_activity_private_key);
                }
            }
        });
    }


    /**
     * 检查参数
     *
     * @param MemorizingWord 活动私钥
     * @param pwd            密码
     * @param againPwd       再次输入的密码
     */
    private boolean checkParams(String MemorizingWord, String pwd, String againPwd) {
        int tip = 0;
        if ("".equals(MemorizingWord) && type == 0) {
            tip = R.string.memorizing_words_not_allow_empty;
        } else if (MemorizingWord.split(" ").length != BrainKey.BRAINKEY_WORD_COUNT && type == 0) {
            tip = R.string.memorizing_words_count_error;
        } else if ("".equals(MemorizingWord) && type == 1) {
            tip = R.string.private_key_not_allow_empty;
        } else if ("".equals(pwd)) {
            tip = R.string.password_not_allow_empty;
        } else if ("".equals(againPwd)) {
            tip = R.string.please_enter_password_again;
        } else if (!pwd.equals(againPwd)) {
            tip = R.string.password_not_match;
        }
        if (tip != 0) {
            showError(getString(tip));
            return false;
        }
        return true;
    }

    @OnClick(R.id.import_account_btn)
    void onImportAccountBtnClick() {
        String memorizingWord = memorizingWordsEt.getText().toString();
        String pwd = passwordEt.getText().toString();
        String againPwd = againPasswordEt.getText().toString();
        boolean isPass = checkParams(memorizingWord, pwd, againPwd);
        if (isPass) {
            String wifKey = memorizingWord;
            if (type == 0) {
                wifKey = WalletService.getInstance().getWifKey(new BrainKey(memorizingWord, BrainKey.DEFAULT_SEQUENCE_NUMBER));
            }
            showLoading(mloadingId, R.string.geting_account);
            mPresenter.importAccount(wifKey, pwd);
        }
    }

    @Override
    public void onImportAccountSuccess(List<WalletData> wallet) {
        dismissLoading(mloadingId);
        MainActivity.start(this);
        finish();
    }

    @Override
    protected ImportWalletContract.Presenter initPresenter() {
        return new ImportWalletPresenter(this);
    }

}
