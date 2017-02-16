package com.appbook.book.entity;

import java.io.Serializable;


public class ProjectInfo implements Serializable{
    private int id;
    private String project_name;
    private String project_logo;
    private int project_id;
    private String project_index;

    public int getId() {
        return id;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getProject_logo() {
        return project_logo;
    }

    public int getProject_id() {
        return project_id;
    }

    public String getProject_index() {
        return project_index;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setProject_logo(String project_logo) {
        this.project_logo = project_logo;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setProject_index(String project_index) {
        this.project_index = project_index;
    }
}
