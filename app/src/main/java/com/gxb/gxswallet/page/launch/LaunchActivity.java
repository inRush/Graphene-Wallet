package com.gxb.gxswallet.page.launch;

import com.gxb.gxswallet.R;
import com.gxb.gxswallet.db.wallet.WalletDataManager;
import com.gxb.gxswallet.page.firstin.FirstInActivity;
import com.gxb.gxswallet.page.firstin.PermissionsFragment;
import com.gxb.gxswallet.page.main.MainActivity;
import com.sxds.common.app.BaseActivity;

import java.util.List;

/**
 * @author inrush
 * @date 2018/3/16.
 */

public class LaunchActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_lanuch;
    }

    private void permissionAllPass() {
        boolean hasAccount = new WalletDataManager(this).queryAll().size() > 0;
        if (hasAccount) {
            MainActivity.start(this);
        } else {
            FirstInActivity.start(LaunchActivity.this);
        }
        finish();
    }

    private void checkPermissions() {
        if (PermissionsFragment.havAllPermissions(LaunchActivity.this)) {
            permissionAllPass();
        } else {
            PermissionsFragment.show(getSupportFragmentManager(), new PermissionsFragment.onPermissionGrantedListener() {
                @Override
                public void onGranted(List<String> perms) {
                    permissionAllPass();
                }
            });
        }

    }

    @Override
    protected void initData() {
        super.initData();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissions();
            }
        }, 2000);
    }
}
