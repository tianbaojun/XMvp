package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/19.
 */
public class MeetingBean implements Serializable{

    private String title = "";
    private String eventTime= "";
    private String content = "";
    private String createTime= "";
    private String nickName = "";
    private String unReadNum = "0";
    private String statusNum = "0";
    private String createUser= "";
    private String msgId = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }






}
