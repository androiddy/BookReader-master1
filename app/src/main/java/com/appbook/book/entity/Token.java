package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/11/12.
 */

public class Token {
    public static final String ID = "id";
    public static final String TOKEN = "token";
    public static final String SIGN = "sign";
    public String token;
    public String sign;
    public String getToken() {
        return token;
    }

    public String getSign() {
        return sign;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
