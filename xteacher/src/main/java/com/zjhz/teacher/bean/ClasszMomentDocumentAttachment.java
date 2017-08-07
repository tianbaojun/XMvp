package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/26.
 * Description:
 * What Changed:
 */

public class ClasszMomentDocumentAttachment implements Serializable {

    private String attPath,attExtName,createTime,attSort,attName,dId,createUser,attId;

    private String picPath,suffName;
    //音频时长
    private String remarks = "0";

    public String getRemarks() {
        return remarks;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getSuffName() {
        return suffName;
    }

    public void setSuffName(String suffName) {
        this.suffName = suffName;
    }

    public String getAttPath() {
        return attPath;
    }

    public void setAttPath(String attPath) {
        this.attPath = attPath;
    }

    public String getAttExtName() {
        return attExtName;
    }

    public void setAttExtName(String attExtName) {
        this.attExtName = attExtName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAttSort() {
        return attSort;
    }

    public void setAttSort(String attSort) {
        this.attSort = attSort;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }
}
