package com.iwhys.cnode.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iwhys.cnode.R;

/**
 * 监听滑动事件的WebView
 * Created by devil on 14-7-28.
 */
public class ScrollListenerWebView extends WebView {

    private OnScrollListener onScrollListener;

    public ScrollListenerWebView(Context context){
        this(context, null);
    }

    public ScrollListenerWebView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public ScrollListenerWebView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        setWebView();
    }

    public void setOnScrollListener(OnScrollListener listener){
        onScrollListener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener!=null){
            onScrollListener.onScrolled(l, t, oldl, oldt);
        }
    }

    /**
     * 滚动监听器
     */
    public interface OnScrollListener{
        void onScrolled(int l, int t, int oldl, int oldt);
    }

    // webView设置
    private void setWebView() {
        setBackgroundResource(R.color.web_view_bg);
        final WebSettings webSettings = getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(false);
//        webSettings.setBlockNetworkLoads(true);
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent in = new Intent (Intent.ACTION_VIEW , Uri.parse(url));
                getContext().startActivity(in);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                webSettings.setBlockNetworkLoads(false);
            }
        });
    }
}
