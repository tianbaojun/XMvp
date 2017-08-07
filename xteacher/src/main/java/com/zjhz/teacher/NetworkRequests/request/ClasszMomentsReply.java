package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsReply implements Serializable {
    private String brepUserId,dId,commentContent,parentId,roleType,userId;

    public ClasszMomentsReply(String brepUserId, String dId, String commentContent, String parentId, String roleType, String userId) {
        this.brepUserId = brepUserId;
        this.dId = dId;
        this.commentContent = commentContent;
        this.parentId = parentId;
        this.roleType = roleType;
        this.userId = userId;
    }

    public String getBrepUserId() {
        return brepUserId;
    }

    public String getParentId() {
        return parentId;
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
}
