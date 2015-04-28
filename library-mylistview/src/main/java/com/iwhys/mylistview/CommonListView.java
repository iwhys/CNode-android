package com.iwhys.mylistview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.iwhys.mylistview.jazzy.JazzyEffect;

import java.util.List;

/**
 * 通用的ListView，功能包括：下拉刷新，上啦加载更多，加载失败或无数据处理
 * 使用时需要实现：1.获取数据的url，2.获取数据并转换成实体，3.适配器
 * Created by devil on 15/4/9.
 */
public abstract class CommonListView<T> implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {

    //上下文
    private Context context;
    //自动刷新间隔，默认5分钟
    private long refreshInterval = 5 * 60;
    //数据适配器
    private BaseListAdapter<T> adapter;
    //记录上次刷新成功的时间
    private long refreshTime = 0;
    //下拉刷新
    private SwipeRefreshLayout refreshLayout;
    //列表控件
    private LoadMoreListView listView;
    //空值View
    private View container, emptyView;
    //空值提示
    private TextView emptyHint;
    //是否正在刷新
    private boolean refreshing = false;
    //当前请求的页码
    private int currentPage = 1;

    public CommonListView(Context context) {
        this.context = context;
        init();
    }

    /**
     * 获取布局文件
     *
     * @return 布局
     */
    public View getView() {
        return container;
    }

    /**
     * 设置自动刷新时间
     *
     * @param interval 时间间隔
     */
    public void setRefreshInterval(long interval) {
        this.refreshInterval = interval;
    }

    public void scrollToBottom() {
        listView.setSelection(listView.getBottom());
    }

    /**
     * 刷新
     *
     * @param rightNow 是否立即刷新 false时判断刷新间隔是否满足自动刷新条件
     */
    public void refresh(boolean rightNow) {
        if (emptyView.getVisibility() == View.VISIBLE) {
            emptyView.setVisibility(View.GONE);
        }
        //如果正在加载更多或者正在刷新或者不满足自动刷新条件，取消操作
        if (listView.isLoadingMore() || refreshing || (!rightNow && (System.currentTimeMillis() / 1000 - refreshTime) < refreshInterval)) {
            return;
        }
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        refreshing = true;
        listView.setRefreshing(true);
        getDataFromServer(1);
    }

    /**
     * 获取数据成功
     *
     * @param page     页码
     * @param dataList 返回的数据
     */
    public void onGetDataSuccess(int page, List<T> dataList, long refreshTime) {
        stopRefreshOrLoadMore(page, true);
        if (dataList == null || dataList.isEmpty()) {
            noData();
        } else {
            if (page == 1) {
                this.refreshTime = refreshTime;
            }
            adapter.refresh(dataList, page != 1);
            currentPage = page;
        }
    }

    /**
     * 获取数据失败
     *
     * @param page 页码
     */
    public void onGetDataFailure(int page) {
        stopRefreshOrLoadMore(page, false);
        refreshOrLoadMoreFailure();
    }

    /**
     * 设置动画效果
     *
     * @param jazzyEffect 效果
     */
    public void setJazzyEffect(JazzyEffect jazzyEffect) {
        listView.setTransitionEffect(jazzyEffect);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        listView.setOnScrollListener(onScrollListener);
    }

    /**
     * 单击事件
     *
     * @param onItemClickListener 监听器
     */
    public void setOnItemClickListener(CompatOnItemClickListener onItemClickListener) {
        listView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * 是否开启加载更多功能
     *
     * @param isEnable 是否
     */
    public void enableLoadMore(boolean isEnable) {
        listView.setEnableLoadMore(isEnable);
    }

    /**
     * 长按事件
     *
     * @param longClickListener 监听器
     */
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener longClickListener) {
        listView.setOnItemLongClickListener(longClickListener);
    }

    //初始化
    private void init() {
        container = View.inflate(context, R.layout.view_listview, null);
        refreshLayout = (SwipeRefreshLayout) container.findViewById(R.id.refresh_container);
        refreshLayout.setOnRefreshListener(this);
        listView = (LoadMoreListView) refreshLayout.findViewById(R.id.list_view);
        listView.setTransitionEffect(jazzyEffect);
        adapter = getAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnLoadMoreListener(this);
        emptyView = container.findViewById(R.id.empty_view);
        emptyHint = (TextView) container.findViewById(R.id.empty_hint);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(false);
            }
        });
        listView.post(new Runnable() {
            @Override
            public void run() {
                getDataFromLocal();
            }
        });
    }

    //列表滑动时的显示效果
    private final JazzyEffect jazzyEffect = new JazzyEffect() {

        private final float SCALE = 0.9f
                ,
                FINAL = 1.0f;

        @Override
        public void initView(View item, int position, int scrollDirection) {
            if (item == null) return;
            item.setScaleX(SCALE);
            item.setScaleY(SCALE);
        }

        @Override
        public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimator animator) {
            if (item == null || animator == null) return;
            animator.scaleX(FINAL);
            animator.scaleY(FINAL);
        }
    };

    //停止刷新或加载更多
    private void stopRefreshOrLoadMore(int page, boolean loadMoreSuccess) {
        if (page == 1) {
            refreshFinished();
        } else {
            listView.stopLoadMore(loadMoreSuccess);
        }
    }

    //刷新或加载更多失败
    private void refreshOrLoadMoreFailure() {
        if (adapter.isEmpty()) {
            emptyHint.setText(R.string.load_failure);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    //没有数据
    private void noData(){
        if (adapter.isEmpty()) {
            emptyHint.setText(R.string.no_data);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 刷新完成
     */
    private void refreshFinished() {
        refreshing = false;
        listView.setRefreshing(false);
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        if (listView.isLoadingMore()) {
            refreshLayout.setRefreshing(false);
            return;
        }
        refresh(true);
    }

    @Override
    public void onLoadMore() {
        getDataFromServer(currentPage + 1);
    }

    /**
     * 获取最终的适配器
     *
     * @return 适配器
     */
    public abstract BaseListAdapter<T> getAdapter(Context context);

    /**
     * 从网络获取数据
     *
     * @param page 页码
     */
    public abstract void getDataFromServer(int page);

    /**
     * 从本地获取数据
     */
    public abstract void getDataFromLocal();
}
