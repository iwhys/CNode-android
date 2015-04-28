package com.iwhys.cnode.adapter;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.iwhys.cnode.ui.fragment.UserTopicListFragment;

import java.util.List;


public class UserColumnPagerAdapter extends FragmentPagerAdapter {

    private List<UserTopicListFragment> mFragments;
    private String[] columnTitles;

    public UserColumnPagerAdapter(FragmentManager manager, List<UserTopicListFragment> fragments, String[] columnTitles) {
        super(manager);
        mFragments = fragments;
        this.columnTitles = columnTitles;
    }

    @Override
    public UserTopicListFragment getItem(int arg0) {
        return mFragments.get(arg0);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return columnTitles[position];
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


}
