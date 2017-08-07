package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 群发消息列表对象
 * Created by Administrator on 2016/7/29.
 */
public class MassgeBean implements Serializable{
    private String content ="";
    private String createTime = "";
    private String createUser = "";
    private String nickName = "";
    private String userNames = "";
    private String statusNum = "";
    private String unReadNum = "";
    private String msgId = "";

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(String unReadNum) {
        this.unReadNum = unReadNum;
    }

    public String getStatusNum() {
        return statusNum;
    }

    public void setStatusNum(String statusNum) {
        this.statusNum = statusNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
