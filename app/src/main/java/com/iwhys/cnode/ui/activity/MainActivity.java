package com.iwhys.cnode.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.iwhys.cnode.App;
import com.iwhys.cnode.R;
import com.iwhys.cnode.adapter.ViewPagerAdapter;
import com.iwhys.cnode.ui.fragment.LeftMenuFragment;
import com.iwhys.cnode.ui.fragment.NewTopicFragment;
import com.iwhys.cnode.ui.fragment.TopicListFragment;
import com.iwhys.cnode.util.ActivitySwitcher;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.DoubleClickExitHelper;
import com.iwhys.cnode.util.OauthHelper;
import com.iwhys.cnode.util.SimpleFactory;
import com.iwhys.cnode.util.constant.IntentAction;
import com.iwhys.cnode.util.constant.Params;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    //按两次返回键退出
    private DoubleClickExitHelper doubleClickExitHelper;
    //抽屉布局类
    private DrawerLayout drawerLayout;
    //主内容容器
    private ViewPager viewPager;
    //栏目标签
    private PagerSlidingTabStrip tabs;
    //页面适配器
    private ViewPagerAdapter pagerAdapter;
    //左侧菜单
    private LeftMenuFragment leftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doubleClickExitHelper = new DoubleClickExitHelper(this);
        setContentView(R.layout.activity_main);
        initActionBarAndDrawer();
        initLayout();
        //所有控件加载完开始加载数据
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                refreshItem();
            }
        });
        IntentFilter filter = new IntentFilter(IntentAction.LOGIN);
        filter.addAction(IntentAction.NEW_TOPIC);
        registerReceiver(myReceiver, filter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            refreshItem();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                toggleDrawer();
                return true;
            }
            return doubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            if (OauthHelper.needLogin()) {
                OauthHelper.showLogin(this);
            } else {
                ActivitySwitcher.pushFragment(this, NewTopicFragment.class);
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.add).setVisible(!(drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)));
        return super.onPrepareOptionsMenu(menu);
    }

    private void initActionBarAndDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View view) {
                if (!TextUtils.isEmpty(App.getContext().access_token)) {
                    leftMenu.setUserInfo();
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
        };
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    //获取布局内容
    private void initLayout() {
        String[] columnTabs = getResources().getStringArray(R.array.column_tab);
        ArrayList<TopicListFragment> fragments = new ArrayList<>();
        for (int i = 0; i < columnTabs.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString(Params.TAB, columnTabs[i]);
            TopicListFragment fragment = (TopicListFragment) SimpleFactory.createFragment(TopicListFragment.class.getSimpleName(), bundle);
            fragments.add(i, fragment);
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, getResources().getStringArray(R.array.column_title));
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setTextSize(CommonUtils.sp2px(14));
        tabs.setTabBackground(android.R.color.transparent);
        tabs.setOnPageChangeListener(this);

        //左侧菜单
        leftMenu = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.left_menu, leftMenu).commit();
    }

    //切换抽屉菜单
    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        refreshItem();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //刷新页面
    private void refreshItem() {
        updateTabStatus();
        pagerAdapter.refreshItem(viewPager.getCurrentItem());
    }

    //更新标签状态
    private void updateTabStatus() {
        ViewGroup container = (ViewGroup) tabs.getChildAt(0);
        for (int i = 0; i < container.getChildCount(); i++) {
            TextView child = (TextView) container.getChildAt(i);
            child.setTextColor(getResources().getColor(i == viewPager.getCurrentItem() ? R.color.tab_selected : R.color.tab_normal));
            child.setTypeface(Typeface.defaultFromStyle(i == viewPager.getCurrentItem() ? Typeface.BOLD : Typeface.NORMAL), 0);
        }
    }

    //广播监听器
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(IntentAction.LOGIN)) {
                leftMenu.setUserInfo();
            } else if (action.equals(IntentAction.NEW_TOPIC)) {
                pagerAdapter.getItem(0).refresh(true);
            }
        }
    };

}
