package com.iwhys.cnode.widget;

import android.webkit.WebView;
import android.content.Context;
import android.util.AttributeSet;

/**
 * 监听滑动事件的WebView
 * Created by devil on 14-7-28.
 */
public class ScrollListenerWebView extends WebView {

    private OnScrollListener onScrollListener;

    public ScrollListenerWebView(Context context){
        super(context);
    }

    public ScrollListenerWebView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public ScrollListenerWebView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
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
}
