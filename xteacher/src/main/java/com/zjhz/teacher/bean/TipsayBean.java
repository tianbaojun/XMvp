package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/16.
 * 代课老师
 */

public class TipsayBean implements Serializable {
    private String name;  // 代课老师
    private String teacherId;
    private String phone;

    public String getName() {
        return name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
