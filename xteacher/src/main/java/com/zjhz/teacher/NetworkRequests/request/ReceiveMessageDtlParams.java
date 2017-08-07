package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 消息群发详情
 * Created by Administrator on 2016/8/2.
 */
public class ReceiveMessageDtlParams implements Serializable{
    private String msgId ;
    private String phoneNo ;

    public ReceiveMessageDtlParams(String msgId, String phoneNo) {
        this.msgId = msgId;
        this.phoneNo = phoneNo;
    }
    //    public ReceiveMessageDtlParams(String msgId) {
//        this.msgId = msgId;
//    }
}
