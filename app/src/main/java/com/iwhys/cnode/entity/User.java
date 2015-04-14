package com.iwhys.cnode.entity;

import java.util.List;

/**
 * 作者类
 * Created by devil on 15/4/1.
 */
public class User extends Base {

    private Author author;
    private String githubUsername;
    private String score;
    private List<Topic> recentTopics;
    private List<Topic> recentReplies;
    private List<Topic> collectionTopics;
}
