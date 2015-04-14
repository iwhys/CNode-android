package com.iwhys.cnode.entity;

import java.util.List;

/**
 * 主题列表
 * Created by devil on 15/4/2.
 */
public class Topics {
    private List<Topic> data;

    public List<Topic> getData() {
        return data;
    }

    public void setData(List<Topic> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Topics{" +
                "data=" + data +
                '}';
    }
}
