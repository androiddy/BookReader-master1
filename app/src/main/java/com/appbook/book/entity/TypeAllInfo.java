package com.appbook.book.entity;

import java.io.Serializable;

/**
 * Created by zhangzhongping on 16/10/29.
 */

public class TypeAllInfo implements Serializable {
    public int id;
    public int type_id;
    public String type_name;
    public int type_number;
    public String type_logo;
    public String type_logo1;
    public String type_logo2;

    public void setType_logo1(String type_logo1) {
        this.type_logo1 = type_logo1;
    }

    public void setType_logo2(String type_logo2) {
        this.type_logo2 = type_logo2;
    }

    public String getType_logo1() {
        return type_logo1;
    }

    public String getType_logo2() {
        return type_logo2;
    }

    public int getId() {
        return id;
    }

    public int getType_id() {
        return type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public int getType_number() {
        return type_number;
    }

    public String getType_logo() {
        return type_logo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public void setType_number(int type_number) {
        this.type_number = type_number;
    }

    public void setType_logo(String type_logo) {
        this.type_logo = type_logo;
    }
}
