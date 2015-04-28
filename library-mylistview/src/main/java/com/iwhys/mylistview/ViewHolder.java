package com.iwhys.mylistview;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by devil on 15/2/12.
 * 适用于Adapter的通用ViewHolder
 */
public class ViewHolder {

    private SparseArray<View> views;
    private View convertView;

    private ViewHolder(View convertView) {
        this.views = new SparseArray<>();
        this.convertView = convertView;
        convertView.setTag(this);
    }

    /**
     * 静态方法获取ViewHolder
     * @param convertView convertView
     * @return ViewHolder
     */
    public static ViewHolder get(View convertView) {
        if (convertView == null) {
            throw new NullPointerException("必须先初始化convertView.");
        }
        Object viewHolder = convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        return (ViewHolder) viewHolder;
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

    /**
     * 设置文本内容
     * @param viewId 文本控件id
     * @param text 文本内容
     * @return viewHolder
     */
    public ViewHolder setText(int viewId, CharSequence text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

}
