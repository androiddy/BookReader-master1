package com.appbook.book.entity;


import org.json.JSONArray;

/**
 * Created by zhangzhongping on 16/10/28.
 */

public class BookResult {
    public String msg;
    public int status;
    public Object data;
    public int number;
    private  String session;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
