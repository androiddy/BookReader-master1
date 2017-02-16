package com.appbook.book.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by zhangzhongping on 16/11/27.
 */

public class MessageInfoIndex implements Serializable{
    private int id;
    private String message_name;
    private int message_type;
    private String message_typename;
    private long message_time;
    private int message_view;
    @JSONField(name = "message_logo")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getMessage_name() {
        return message_name;
    }

    public int getMessage_type() {
        return message_type;
    }

    public String getMessage_typename() {
        return message_typename;
    }

    public long getMessage_time() {
        return message_time;
    }

    public int getMessage_view() {
        return message_view;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage_name(String message_name) {
        this.message_name = message_name;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public void setMessage_typename(String message_typename) {
        this.message_typename = message_typename;
    }

    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }

    public void setMessage_view(int message_view) {
        this.message_view = message_view;
    }
}
