package com.appbook.book.entity;

/**
 * Created by zhangzhongping on 16/12/2.
 */

public class BannerInfo {
    private int id;
    private int banner_id;
    private int banner_type;
    private int banner_click;
    private String banner_name;
    private String banner_image;
    private String banner_typename;

    public void setId(int id) {
        this.id = id;
    }

    public void setBanner_id(int banner_id) {
        this.banner_id = banner_id;
    }

    public void setBanner_type(int banner_type) {
        this.banner_type = banner_type;
    }

    public void setBanner_click(int banner_click) {
        this.banner_click = banner_click;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public void setBanner_typename(String banner_typename) {
        this.banner_typename = banner_typename;
    }

    public int getId() {
        return id;
    }

    public int getBanner_id() {
        return banner_id;
    }

    public int getBanner_type() {
        return banner_type;
    }

    public int getBanner_click() {
        return banner_click;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public String getBanner_typename() {
        return banner_typename;
    }
}
