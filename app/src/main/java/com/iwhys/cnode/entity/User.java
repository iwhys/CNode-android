package com.iwhys.cnode.entity;

import java.util.Date;
import java.util.List;

/**
 * 用户类
 * Created by devil on 15/4/1.
 */
public class User extends Base {

    private String loginname;
    private String score;
    private List<Topic> recent_topics;
    private List<Topic> recent_replies;
    private List<Topic> collect_topics;
    private Date create_at;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<Topic> getRecent_topics() {
        return recent_topics;
    }

    public void setRecent_topics(List<Topic> recent_topics) {
        this.recent_topics = recent_topics;
    }

    public List<Topic> getRecent_replies() {
        return recent_replies;
    }

    public void setRecent_replies(List<Topic> recent_replies) {
        this.recent_replies = recent_replies;
    }

    public List<Topic> getCollect_topics() {
        return collect_topics;
    }

    public void setCollect_topics(List<Topic> collect_topics) {
        this.collect_topics = collect_topics;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }
}
