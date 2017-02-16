package com.appbook.book.entity;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class mBookInfo implements Serializable {
    public int book_id;
    @JSONField(name = "book_logo")
    public String logo;
    public String book_name;
    public String book_size;
    public String book_author;
    public String book_introduction;
    public String book_dowurl;
    public String book_number;
    public int book_type;
    @JSONField(name = "book_typename")
    public String book_ytpename;
    public long book_time;
    public long book_byte;
    public int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getBook_byte() {
        return book_byte;
    }

    public void setBook_byte(long book_byte) {
        this.book_byte = book_byte;
    }

    public long getBook_time() {
        return book_time;
    }

    public void setBook_time(long book_time) {
        this.book_time = book_time;
    }

    public String getBook_ytpename() {
        return book_ytpename;
    }
    public void setBook_ytpename(String book_ytpename) {
        this.book_ytpename = book_ytpename;
    }
    public int getBook_id() {
        return book_id;
    }
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getBook_name() {
        return book_name;
    }
    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }
    public String getBook_size() {
        return book_size;
    }
    public void setBook_size(String book_size) {
        this.book_size = book_size;
    }
    public String getBook_author() {
        return book_author;
    }
    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }
    public String getBook_introduction() {
        return book_introduction;
    }
    public void setBook_introduction(String book_introduction) {
        this.book_introduction = book_introduction;
    }
    public String getBook_dowurl() {
        return book_dowurl;
    }
    public void setBook_dowurl(String book_dowurl) {
        this.book_dowurl = book_dowurl;
    }
    public String getBook_number() {
        return book_number;
    }
    public void setBook_number(String book_number) {
        this.book_number = book_number;
    }
    public int getBook_type() {
        return book_type;
    }
    public void setBook_type(int book_type) {
        this.book_type = book_type;
    }
    @Override
    public String toString() { return "mbook [book_id=" + book_id + ", logo=" + logo + ", book_name=" + book_name + ", book_size=" + book_size + ", book_author=" + book_author + ", book_introduction=" + book_introduction + ", book_dowurl=" + book_dowurl + ", book_number=" + book_number + ", book_type=" + book_type + "]"; }
}