package com.iwhys.cnode.entity;

import java.util.Date;

/**
 * 回复
 * Created by devil on 15/4/10.
 */
public class Reply extends Base {
    private String id;
    private Author author;
    private String content;
    private String ups[];
    private Date create_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getUps() {
        return ups;
    }

    public void setUps(String[] ups) {
        this.ups = ups;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }
}
