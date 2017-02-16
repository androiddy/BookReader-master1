package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/12/17.
 */

public class UserInfo {
    private String user_id;
    private String user_name;
    private String user_logo;
    private String user_token;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public String getUser_token() {
        return user_token;
    }
}
