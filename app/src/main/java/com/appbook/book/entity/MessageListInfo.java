package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/11/27.
 */

public class MessageListInfo {
    private  int id;
    private String mes_name;
    private int mes_id;
    private int mes_number;

    public int getId() {
        return id;
    }

    public String getMes_name() {
        return mes_name;
    }

    public int getMes_id() {
        return mes_id;
    }

    public int getMes_number() {
        return mes_number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMes_name(String mes_name) {
        this.mes_name = mes_name;
    }

    public void setMes_id(int mes_id) {
        this.mes_id = mes_id;
    }

    public void setMes_number(int mes_number) {
        this.mes_number = mes_number;
    }
}
