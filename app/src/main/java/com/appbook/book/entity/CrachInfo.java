package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/12/19.
 */

public class CrachInfo {
    private String time;
    private String appvarname;
    private String appvar;
    private String androidvarname;
    private String androidvar;
    private String phonebrand;
    private String phonemodels;
    private String crachmsg;
    private String cpuabi;

    public void setCpuabi(String cpuabi) {
        this.cpuabi = cpuabi;
    }

    public String getCpuabi() {
        return cpuabi;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAppvarname(String appvarname) {
        this.appvarname = appvarname;
    }

    public void setAppvar(String appvar) {
        this.appvar = appvar;
    }

    public void setAndroidvarname(String androidvarname) {
        this.androidvarname = androidvarname;
    }

    public void setAndroidvar(String androidvar) {
        this.androidvar = androidvar;
    }

    public void setPhonebrand(String phonebrand) {
        this.phonebrand = phonebrand;
    }

    public void setPhonemodels(String phonemodels) {
        this.phonemodels = phonemodels;
    }

    public void setCrachmsg(String crachmsg) {
        this.crachmsg = crachmsg;
    }

    public String getTime() {
        return time;
    }

    public String getAppvarname() {
        return appvarname;
    }

    public String getAppvar() {
        return appvar;
    }

    public String getAndroidvarname() {
        return androidvarname;
    }

    public String getAndroidvar() {
        return androidvar;
    }

    public String getPhonebrand() {
        return phonebrand;
    }

    public String getPhonemodels() {
        return phonemodels;
    }

    public String getCrachmsg() {
        return crachmsg;
    }
}
