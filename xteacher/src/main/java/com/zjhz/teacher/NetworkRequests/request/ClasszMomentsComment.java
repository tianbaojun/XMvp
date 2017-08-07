package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsComment implements Serializable {
    public ClasszMomentsComment(String dId, String commentContent, String roleType, String userId) {
        this.dId = dId;
        this.commentContent = commentContent;
        this.roleType = roleType;
        this.userId = userId;
    }

    private String dId,commentContent,roleType,userId,brepUserId,parentId;

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getdId() {
        return dId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public String getRoleType() {
        return roleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setBrepUserId(String brepUserId) {
        this.brepUserId = brepUserId;
    }
}
