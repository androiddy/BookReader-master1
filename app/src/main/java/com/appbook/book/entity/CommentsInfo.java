package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/12/17.
 */

public class CommentsInfo {
    public int id;
    public String user_id;
    public String user_msg;
    public String user_name;
    public String user_logo;
    public long time;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public long getTime() {
        return time;
    }
}
