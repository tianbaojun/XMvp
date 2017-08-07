package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/25.
 * Description:
 * What Changed:
 */

public class ClasszMomentPariseBean implements Serializable {
    private String cancelPraiseTime,praiseTime,schoolId,dpId,dpStatus,roleType,userName,name,userId,dId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPraiseTime() {
        return praiseTime;
    }

    public void setPraiseTime(String praiseTime) {
        this.praiseTime = praiseTime;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getDpStatus() {
        return dpStatus;
    }

    public void setDpStatus(String dpStatus) {
        this.dpStatus = dpStatus;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getCancelPraiseTime() {
        return cancelPraiseTime;
    }

    public void setCancelPraiseTime(String cancelPraiseTime) {
        this.cancelPraiseTime = cancelPraiseTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }
}
