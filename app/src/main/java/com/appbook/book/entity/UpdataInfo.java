package com.appbook.book.entity;

import java.io.Serializable;

/**
 * Created by zhangzhongping on 16/12/8.
 */

public class UpdataInfo implements Serializable{
    public int id;
    public String appname;
    public String appsize;
    public String dowurl;
    public long appupdatatime;
    public String state;
    public int appvar;
    public int type;
    public int status;
    public String appvarname;
    public String appmd5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAppmd5(String appmd5) {
        this.appmd5 = appmd5;
    }

    public String getAppmd5() {
        return appmd5;
    }

    public void setAppvarname(String appvarname) {
        this.appvarname = appvarname;
    }

    public String getAppvarname() {
        return appvarname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setAppsize(String appsize) {
        this.appsize = appsize;
    }

    public void setDowurl(String dowurl) {
        this.dowurl = dowurl;
    }

    public void setAppupdatatime(long appupdatatime) {
        this.appupdatatime = appupdatatime;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setAppvar(int appvar) {
        this.appvar = appvar;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAppname() {
        return appname;
    }

    public String getAppsize() {
        return appsize;
    }

    public String getDowurl() {
        return dowurl;
    }

    public long getAppupdatatime() {
        return appupdatatime;
    }

    public String getState() {
        return state;
    }

    public int getAppvar() {
        return appvar;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }
}
