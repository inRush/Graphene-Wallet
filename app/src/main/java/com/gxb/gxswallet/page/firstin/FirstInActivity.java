package com.gxb.gxswallet.page.firstin;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.page.createwallet.CreateWalletActivity;
import com.gxb.gxswallet.page.importaccount.ImportWalletActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sxds.common.app.BaseActivity;

import butterknife.OnClick;

/**
 * @author inrush
 * @date 2018/3/13.
 */

public class FirstInActivity extends BaseActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FirstInActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_first_in;
    }

    @Override
    protected void initWindows() {
        super.initWindows();
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
    }

    @OnClick(R.id.import_account)
    void onImportAccountBtnClick(View view) {
        ImportWalletActivity.start(this);
    }

    @OnClick(R.id.create_account)
    void onCreateAccountBtnClick(View view) {
        CreateWalletActivity.start(this);
    }
}
