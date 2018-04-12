package com.gxb.gxswallet.base.webview;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.gxb.gxswallet.R;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sxds.common.app.BaseActivity;

import butterknife.BindView;

/**
 * @author inrush
 * @date 2018/3/24.
 */

public class RefreshAgentWebActivity extends BaseActivity {
    @BindView(R.id.container_refresh_web)
    LinearLayout mLinearLayout;
    @BindView(R.id.topbar_refresh_web)
    QMUITopBar mTopBar;

    private static final String URL_KEY = "url_refresh_web";
    private String mUrl = "https://www.baidu.com";

    private RefreshWebLayout mRefreshWebLayout;
    protected AgentWeb mAgentWeb;
    private AlertDialog mAlertDialog;


    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, RefreshAgentWebActivity.class);
        intent.putExtra(URL_KEY, url);
        activity.startActivity(intent);
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        mTopBar.addLeftImageButton(R.drawable.ic_close_white_24dp,
                View.generateViewId()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTopBar.addRightImageButton(R.drawable.ic_refresh_white_24dp, View.generateViewId())
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAgentWeb.getUrlLoader().reload();
                    }
                });


        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra(URL_KEY);
            if (url != null && !"".equals(url)) {
                mUrl = url;
            }
        }

        this.mRefreshWebLayout = new RefreshWebLayout(this);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) this.mRefreshWebLayout.getLayout();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(getResources().getColor(R.color.colorPrimaryDark), 2)
                // 取消下拉刷新
//                .setWebLayout(this.mRefreshWebLayout)
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                })
                .setWebChromeClient(mWebChromeClient)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .interceptUnkownUrl()
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .createAgentWeb()
                .ready()
                .go(mUrl);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAgentWeb.getUrlLoader().reload();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTopBar.setTitle(title);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage("您确定要关闭该页面吗?")
                    .setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    })//
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            RefreshAgentWebActivity.this.finish();
                        }
                    }).create();
        }
        mAlertDialog.show();

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_refresh_web;
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}
