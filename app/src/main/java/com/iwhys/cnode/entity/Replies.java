package com.iwhys.cnode.entity;

import java.util.List;

/**
 * Created by devil on 15/4/10.
 */
public class Replies extends Topic {

    private List<Reply> replies;

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
