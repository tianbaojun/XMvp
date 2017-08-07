package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 未读消息数量
 * Created by Administrator on 2016/8/26.
 */
public class UnreadMsgParams implements Serializable{
    private String phoneNo;
    private String msgNature;
    private String msgType;

    public UnreadMsgParams(String phoneNo, String msgNature, String msgType) {
        this.phoneNo = phoneNo;
        this.msgNature = msgNature;
        this.msgType = msgType;
    }
    public UnreadMsgParams(String phoneNo) {
        this.phoneNo = phoneNo;

    }
}
