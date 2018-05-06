package com.gxb.gxswallet.page.launch;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.manager.AssetManager;
import com.gxb.gxswallet.manager.WalletManager;
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
            getWindow().getDecorView().postDelayed(this::init, 3000);
            App.showToast(R.string.network_unavailable);
            return;
        }
        AssetManager.getInstance().init(App.getInstance());
        WalletManager.getInstance().init(App.getInstance());
        WebSocketServicePool.getInstance().initPool(App.getInstance());
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

    /**
     * 等待WebSocket池初始化完成
     */
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
