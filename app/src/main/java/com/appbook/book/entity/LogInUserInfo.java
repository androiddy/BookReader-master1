package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/12/16.
 */

public class LogInUserInfo {
    private int ret;
    private String msg;
    private int is_lost;
    private String nickname;
    private String gender;
    private String province;
    private String city;
    private String figureurl_qq_2;
    private String figureurl_qq_1;

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setIs_lost(int is_lost) {
        this.is_lost = is_lost;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    public int getRet() {
        return ret;
    }

    public String getMsg() {
        return msg;
    }

    public int getIs_lost() {
        return is_lost;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }
}
