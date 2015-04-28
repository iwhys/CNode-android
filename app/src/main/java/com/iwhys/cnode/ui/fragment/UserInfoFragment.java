package com.iwhys.cnode.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.astuetz.PagerSlidingTabStrip;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iwhys.cnode.R;
import com.iwhys.cnode.adapter.ViewPagerAdapter;
import com.iwhys.cnode.ui.activity.BaseBackActivity;
import com.iwhys.cnode.util.CommonUtils;
import com.iwhys.cnode.util.OauthHelper;
import com.iwhys.cnode.util.SimpleFactory;
import com.iwhys.cnode.util.constant.IntentAction;
import com.iwhys.cnode.util.constant.Params;
import com.iwhys.cnode.util.volley.UrlHelper;
import com.iwhys.cnode.util.volley.VolleyErrorHelper;
import com.iwhys.cnode.util.volley.VolleyHelper;

import java.util.ArrayList;

/**
 * 用户信息页
 * Created by devil on 15/4/12.
 */
public class UserInfoFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private String username, avatar_url;
    //主内容容器
    private ViewPager viewPager;
    //栏目标签
    private PagerSlidingTabStrip tabs;
    //页面适配器
    private ViewPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        username = bundle.getString(Params.LOGIN_NAME);
        avatar_url = bundle.getString(Params.AVATAR_URL);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.user_info);
        sActivity.setSupportActionBar(toolbar);
        initLayout(view);
        getUserInfo();
        view.post(new Runnable() {
            @Override
            public void run() {
                updateTabStatus();
            }
        });
        return view;
    }

    //获取布局内容
    private void initLayout(View view) {
        ((SimpleDraweeView) view.findViewById(R.id.avatar)).setImageURI(Uri.parse(UrlHelper.resolve(UrlHelper.HOST, avatar_url)), sActivity);
        ((TextView) view.findViewById(R.id.loginname)).setText(username);
        String[] columnTabs = getResources().getStringArray(R.array.user_column_tab);
        ArrayList<TopicListFragment> fragments = new ArrayList<>();
        for (int i = 0; i < columnTabs.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString(Params.TAB, columnTabs[i]);
            TopicListFragment fragment = (TopicListFragment) SimpleFactory.createFragment(TopicListFragment.class.getSimpleName(), bundle);
            fragments.add(i, fragment);
        }
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments, getResources().getStringArray(R.array.user_column_title));
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setTextSize(CommonUtils.sp2px(14));
        tabs.setTabBackground(android.R.color.transparent);
        tabs.setOnPageChangeListener(this);
        updateTabStatus();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit){
            showExitHint();
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((BaseBackActivity) sActivity).enableGesture(position == 0);
        updateTabStatus();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getUserInfo() {
        showProgress(R.string.on_loading);
        String url = UrlHelper.resolve(UrlHelper.USER, username);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgress();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                VolleyErrorHelper.getMessage(error, sActivity);
            }
        });
        VolleyHelper.addToRequestQueue(request);
    }

    //更新标签状态
    private void updateTabStatus() {
        ViewGroup container = (ViewGroup) tabs.getChildAt(0);
        for (int i = 0; i < container.getChildCount(); i++) {
            TextView child = (TextView) container.getChildAt(i);
            child.setTextColor(getResources().getColor(i == viewPager.getCurrentItem() ? R.color.heavy_color : R.color.tab_normal));
            child.setTypeface(Typeface.defaultFromStyle(i == viewPager.getCurrentItem() ? Typeface.BOLD : Typeface.NORMAL), 0);
        }
    }

    private void showExitHint(){
        AlertDialog.Builder builder = new AlertDialog.Builder(sActivity)
                .setTitle("信息确认")
                .setMessage("是否确定退出？")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
        builder.show();
    }

    private void logout(){
        OauthHelper.logout();
        sActivity.sendBroadcast(new Intent(IntentAction.LOGIN));
        sActivity.finish();
    }
}
