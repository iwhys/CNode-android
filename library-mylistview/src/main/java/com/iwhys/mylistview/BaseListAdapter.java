package com.iwhys.mylistview;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表基类
 * Created by devil on 15/4/9.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    private List<T> dataList;

    public BaseListAdapter(){
        dataList = new ArrayList<>();
    }

    /**
     * 刷新列表
     *
     * @param datas 新获取到得数据列表
     * @param isLoadMore 是否加载更多
     */
    public void refresh(List<T> datas, boolean isLoadMore) {
        if (datas.isEmpty()) return;
        if (!isLoadMore){
            dataList.clear();
        }
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

}
