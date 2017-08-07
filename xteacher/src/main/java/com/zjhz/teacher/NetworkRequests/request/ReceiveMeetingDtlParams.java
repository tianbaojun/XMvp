package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 会议通知详情
 * Created by Administrator on 2016/8/2.
 */
public class ReceiveMeetingDtlParams implements Serializable{
    private String meetId = "";
    private String phoneNo = "";

    public ReceiveMeetingDtlParams(String meetId, String phoneNo) {
        this.meetId = meetId;
        this.phoneNo = phoneNo;
    }

//    public ReceiveMeetingDtlParams(String meetId) {
//        this.meetId = meetId;
//    }
}
