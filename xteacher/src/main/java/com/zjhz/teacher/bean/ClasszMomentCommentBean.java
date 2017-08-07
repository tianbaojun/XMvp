package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/25.
 * Description:
 * What Changed:
 */

public class ClasszMomentCommentBean implements Serializable {
    private String brepUserId,brepUserName,parentId,dcStatus,dcId,schoolId,commentContent,roleType,userName,commentTime,userId,dId,name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrepUserId() {
        return brepUserId;
    }

    public void setBrepUserId(String brepUserId) {
        this.brepUserId = brepUserId;
    }

    public String getBrepUserName() {
        return brepUserName;
    }

    public void setBrepUserName(String brepUserName) {
        this.brepUserName = brepUserName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDcStatus() {
        return dcStatus;
    }

    public void setDcStatus(String dcStatus) {
        this.dcStatus = dcStatus;
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
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
