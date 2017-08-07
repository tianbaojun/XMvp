package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 学校对象
 * Created by Administrator on 2016/7/22.
 */
public class SchoolBean implements Serializable{
    private String schoolName = "";
    private String domain = "";
    private String address = "";
    private String imageUrl = "";

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
