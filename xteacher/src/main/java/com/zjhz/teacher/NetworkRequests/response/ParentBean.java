package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/20.
 */
public class ParentBean implements Serializable{
    private String parentName ="";
    private String phone = "";
    private String userId = "";

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
