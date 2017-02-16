package com.appbook.book.entity;


/**
 * Created by zhangzhongping on 16/11/27.
 */

public class MessageResultInfo {
    private  int status;
    private String msg;
    private Object data;
    private int number;
    private Object image;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public int getNumber() {
        return number;
    }

    public Object getImage() {
        return image;
    }
}
