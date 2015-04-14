package com.iwhys.mylistview;

import android.view.View;
import android.widget.AdapterView;

/**
 * 列表条目单击事件辅助类
 * 屏蔽短时间内多次点击时间
 * Created by devil on 15/4/9.
 */
public abstract class CompatOnItemClickListener implements AdapterView.OnItemClickListener{

    private final static long TIME_INTERVAL = 500;

    private long lastClickTime = 0;

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < TIME_INTERVAL) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isFastDoubleClick()) return;
        onItemClick(view, position);
    }

    /**
     * 列表单击事件
     * @param view view
     * @param position 位置
     */
    public abstract void onItemClick(View view, int position);
}
