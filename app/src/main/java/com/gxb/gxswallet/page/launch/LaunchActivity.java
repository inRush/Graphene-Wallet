package com.gxb.gxswallet.page.launch;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.common.WalletManager;
import com.gxb.gxswallet.db.asset.AssetDataManager;
import com.gxb.gxswallet.page.firstin.FirstInActivity;
import com.gxb.gxswallet.page.firstin.PermissionsFragment;
import com.gxb.gxswallet.page.main.MainActivity;
import com.gxb.gxswallet.services.rpc.WebSocketServicePool;
import com.sxds.common.app.BaseActivity;
import com.sxds.common.utils.NetworkUtil;

import net.qiujuer.genius.kit.handler.Run;

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
        init();
    }

    private void checkPermissions() {
        if (PermissionsFragment.havAllPermissions(LaunchActivity.this)) {
            permissionAllPass();
        } else {
            PermissionsFragment.show(getSupportFragmentManager(), perms -> permissionAllPass());
        }
    }

    private void init() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            getWindow().getDecorView().postDelayed(this::init, 2000);
            App.showToast(R.string.network_unavailable);
            return;
        }
        AssetDataManager.initCoin(this);
        WalletManager.getInstance().init(this);
        WebSocketServicePool.getInstance().initPool();
        Run.onBackground(() -> {
            waitWebSocketPoolComplete();
            boolean hasAccount = WalletManager.getInstance().getAllWallet().size() > 0;
            if (hasAccount) {
                MainActivity.start(LaunchActivity.this);
            } else {
                FirstInActivity.start(LaunchActivity.this);
            }
            finish();
        });

    }

    private void waitWebSocketPoolComplete() {
        try {
            Thread.sleep(1500);
            if (!WebSocketServicePool.getInstance().checkAllServiceConnectSuccess()) {
                waitWebSocketPoolComplete();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initData() {
        super.initData();
        getWindow().getDecorView().postDelayed(this::checkPermissions, 1000);
    }
}
