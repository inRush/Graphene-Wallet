package com.gxb.gxswallet.base.webview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.gxb.gxswallet.R;
import com.just.agentweb.IWebLayout;

/**
 * @author inrush
 * @date 2018/3/24.
 */

public class RefreshWebLayout implements IWebLayout {

    private SwipeRefreshLayout mRefreshLayout;
    private WebView mWebView;

    public RefreshWebLayout(Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_refresh_web_view, null);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mWebView = mRefreshLayout.findViewById(R.id.web_view);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mRefreshLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }
}
