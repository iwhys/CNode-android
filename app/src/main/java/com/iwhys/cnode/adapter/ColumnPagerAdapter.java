package com.iwhys.cnode.adapter;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.iwhys.cnode.ui.fragment.TopicListFragment;

import java.util.List;


public class ColumnPagerAdapter extends FragmentPagerAdapter {

    private List<TopicListFragment> mFragments;
    private String[] columnTitles;

    public ColumnPagerAdapter(FragmentManager manager, List<TopicListFragment> fragments, String[] columnTitles) {
        super(manager);
        mFragments = fragments;
        this.columnTitles = columnTitles;
    }

    /**
     * 刷新页面
     * @param position 位置
     */
    public void refreshItem(int position){
        getItem(position).refresh(false);
    }

    @Override
    public TopicListFragment getItem(int arg0) {
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
