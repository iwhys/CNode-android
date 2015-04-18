package com.iwhys.mylistview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iwhys.mylistview.jazzy.JazzyEffect;
import com.iwhys.mylistview.jazzy.JazzyHelper;


/**
 * 带动画的ListView
 * Created by devil on 14/12/8.
 */
public class LoadMoreListView extends ListView {

    private int mCurrentScrollState;
    private OnLoadMoreListener loadMoreListener;
    //是否正在刷新，是否正在加载更多，是否开启加载更多
    private boolean refreshing = false, loadingMore = false, enableLoadMore = true;
    private final JazzyHelper mHelper;
    private final Footer footer;

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = getHelper(context, attrs);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mCurrentScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean canLoadMore = (firstVisibleItem + visibleItemCount) >= totalItemCount;
                if (enableLoadMore && !refreshing && !loadingMore && canLoadMore
                        && mCurrentScrollState != SCROLL_STATE_IDLE) {
                    startLoadMore();
                }
            }
        });
        footer = new Footer(context);
        footer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadMore();
            }
        });
        addFooterView(footer);
    }

    /**
     * 是否开启加载更多功能
     *
     * @param enableLoadMore 是否
     */
    public void setEnableLoadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }

    public boolean isLoadingMore(){
        return loadingMore;
    }

    /**
     * loadMore listener
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * 停止加载更多
     *
     * @param isLoadMore 是否载入成功
     */
    public void stopLoadMore(boolean isLoadMore) {
        loadingMore = false;
        footer.setStatus(isLoadMore ? Footer.SUCCESS : Footer.FAILURE);
    }

    private JazzyHelper getHelper(Context context, AttributeSet attrs) {
        JazzyHelper helper = new JazzyHelper(context, attrs);
        super.setOnScrollListener(helper);
        return helper;
    }

    private void startLoadMore() {
        loadingMore = true;
        footer.setStatus(Footer.LOADING);
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mHelper.setOnScrollListener(l);
    }

    /**
     * Sets the desired transition effect.
     *
     * @param transitionEffect The non-bundled transition provided by the client.
     */
    public void setTransitionEffect(JazzyEffect transitionEffect) {
        mHelper.setTransitionEffect(transitionEffect);
    }

    /**
     * Sets whether new items or all items should be animated when they become visible.
     *
     * @param onlyAnimateNew True if only new items should be animated; false otherwise.
     */
    public void setShouldOnlyAnimateNewItems(boolean onlyAnimateNew) {
        mHelper.setShouldOnlyAnimateNewItems(onlyAnimateNew);
    }

    /**
     * If true animation will only occur when scrolling without the users finger on the screen.
     *
     * @param onlyFling d
     */
    public void setShouldOnlyAnimateFling(boolean onlyFling) {
        mHelper.setShouldOnlyAnimateFling(onlyFling);
    }

    /**
     * Stop animations after the list has reached a certain velocity. When the list slows down
     * it will start animating again. This gives a performance boost as well as preventing
     * the list from animating under the users finger if they suddenly stop it.
     *
     * @param itemsPerSecond, set to JazzyHelper.MAX_VELOCITY_OFF to turn off max velocity.
     *                        While 13 is a good default, it is dependent on the size of your items.
     */
    public void setMaxAnimationVelocity(int itemsPerSecond) {
        mHelper.setMaxAnimationVelocity(itemsPerSecond);
    }

    /**
     * Enable this if you are using a list with items that should act like grid items.
     *
     * @param simulateGridWithList d
     */
    public void setSimulateGridWithList(boolean simulateGridWithList) {
        mHelper.setSimulateGridWithList(simulateGridWithList);
        setClipChildren(!simulateGridWithList);
    }

    /**
     * 底部View，显示加载更多
     */
    class Footer extends LinearLayout {

        public final static int SUCCESS = 0, FAILURE = 1, LOADING = 2, IDLE = 3;
        private ImageView icon;
        private TextView text;
        private Animation loadingAnim;

        public Footer(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER);
            icon = new ImageView(context);
            icon.setImageResource(R.drawable.icon_loading_small);
            icon.setVisibility(INVISIBLE);
            addView(icon);
            text = new TextView(context);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            text.setTextColor(Color.GRAY);
            text.setPadding(5, 8, 5, 8);
            addView(text);
            loadingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_loading);
            post(new Runnable() {
                @Override
                public void run() {
                    setStatus(Footer.IDLE);
                }
            });
        }

        /**
         * 设置状态
         * @param status 状态
         */
        public void setStatus(int status) {
            switch (status) {
                case SUCCESS:
                    canVisible(false);
                    break;
                case FAILURE:
                    icon.clearAnimation();
                    icon.setVisibility(GONE);
                    text.setText(R.string.load_failure);
                    canVisible(true);
                    break;
                case LOADING:
                    icon.setVisibility(VISIBLE);
                    icon.startAnimation(loadingAnim);
                    text.setText(R.string.on_loading);
                    canVisible(true);
                    break;
                default:
                    canVisible(false);
                    break;
            }
        }

        //设置是否显示，通过padding修正隐藏之后的空白问题
        private void canVisible(boolean visible) {
            setVisibility(visible ? VISIBLE : GONE);
            setPadding(0, visible ? 0 : -getHeight(), 0, 0);
        }
    }

}
