package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 消息回复对象
 * Created by xiangxue on 2016/6/16.
 */
public class NewsReplayBean implements Serializable{
    private String replyId = "";
    private String replyUser = "";
    private String userName;
    private String replyTime= "";
    private String msgContect= "";
    private String userPhotoUrl = "";
    private String name = "";

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getMsgContect() {
        return msgContect;
    }

    public void setMsgContect(String msgContect) {
        this.msgContect = msgContect;
    }
}
