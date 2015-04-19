package com.iwhys.mylistview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by devil on 15/2/12.
 * 适用于Adapter的通用ViewHolder
 */
public class CommonViewHolder {

    private SparseArray<View> views;
    private View convertView;

    private CommonViewHolder(Context context, ViewGroup parent, int layoutId) {
        convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.views = new SparseArray<>();
        convertView.setTag(this);
    }

    /**
     * 静态方法获取ViewHolder
     * @param convertView convertView
     * @return ViewHolder
     */
    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null){
            return new CommonViewHolder(context, parent, layoutId);
        } else {
            return (CommonViewHolder) convertView.getTag();
        }
    }

    /**
     * 根据单个控件的id获取控件
     * @param viewId 控件id
     * @param <T> 继承自View的泛型
     * @return 返回目标控件
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null){
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

}
