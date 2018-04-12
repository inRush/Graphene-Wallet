package com.gxb.gxswallet.page.main;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.gxb.gxswallet.base.activity.ExchangeServiceActivity;
import com.gxb.gxswallet.page.contacts.ContactsFragment;
import com.gxb.gxswallet.page.contacts.OnContactChangeListener;
import com.gxb.gxswallet.page.home.HomeFragment;
import com.gxb.gxswallet.page.my.MyFragment;
import com.gxb.gxswallet.page.quotation.QuotationFragment;
import com.gxb.gxswallet.services.exchange.ExchangeService;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jwsd.libzxing.QRCodeManager;
import com.sxds.common.helper.NavHelper;

import java.util.List;

import butterknife.BindView;


/**
 * @author inrush
 */
public class MainActivity extends ExchangeServiceActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener, OnContactChangeListener, ExchangeServiceProvider {

    @BindView(R.id.bottom_navigation_view_main)
    BottomNavigationViewEx mBottomView;

    private static final int EXIT_TIME_LIMIT = 2000;
    private NavHelper mNavHelper;
    private long clickTime = 0;


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        // 关闭位移动画
        mBottomView.enableShiftingMode(false);
        mBottomView.enableItemShiftingMode(false);
        mBottomView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mNavHelper = new NavHelper<Integer>(this,
                getSupportFragmentManager(), R.id.container_main, this);
        mNavHelper.add(R.id.menu_home, new NavHelper.Tab<>(HomeFragment.class, R.id.menu_home));
        mNavHelper.add(R.id.menu_quotation, new NavHelper.Tab<>(QuotationFragment.class, R.id.menu_quotation));
        mNavHelper.add(R.id.menu_contacts, new NavHelper.Tab<>(ContactsFragment.class, R.id.menu_contacts));
        mNavHelper.add(R.id.menu_my, new NavHelper.Tab<>(MyFragment.class, R.id.menu_my));
        mNavHelper.performClickMenu(R.id.menu_home);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mNavHelper.performClickMenu(item.getItemId());
        return true;
    }

    @Override
    public void onTabChanged(NavHelper.Tab newTab, NavHelper.Tab oldTab) {

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else { // 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > EXIT_TIME_LIMIT) {
            App.showToast(R.string.click_again_exit);
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }
    /**
     * 刷新联系人列表
     */
    private void refreshContactList() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment fragment : fragments){
            if (fragment instanceof ContactsFragment) {
                ((ContactsFragment) fragment).loadContactList();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContactsFragment.CONTACT_REQUEST_CODE) {
            refreshContactList();
            return;
        }
        //注册onActivityResult
        QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected Intent setAction(Intent intent) {
        intent.putExtra(ExchangeService.RUN_TYPE_KEY, ExchangeService.RUN_TYPE_FETCH_LIST);
        return intent;
    }

    @Override
    public void onChange() {
        refreshContactList();
    }


    @Override
    public ExchangeService getService() {
        return mExchangeService;
    }
}
