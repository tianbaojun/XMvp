package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/2.
 */
public class JPushBean implements Serializable{
    private String msgId = "";
    private String meetId = "";
    private int forcedOffLine = -1;
    private int mType = -1;//1表示危险区域、2学生出入校通知

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public int getForcedOffLine() {
        return forcedOffLine;
    }

    public void setForcedOffLine(int forcedOffLine) {
        this.forcedOffLine = forcedOffLine;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMeetId() {
        return meetId;
    }

    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }
}
