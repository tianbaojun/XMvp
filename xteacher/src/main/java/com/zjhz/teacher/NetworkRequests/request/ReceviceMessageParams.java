package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 接收消息列表
 * Created by Administrator on 2016/8/15.
 */
public class ReceviceMessageParams implements Serializable{
    private int page;
    private int pageSize;
    private String phoneNo = "";
    private String msgNature = "";
    private String msgType;

    public ReceviceMessageParams(int page, int pageSize, String phoneNo, String msgNature) {
        this.page = page;
        this.pageSize = pageSize;
        this.phoneNo = phoneNo;
        this.msgNature = msgNature;
    }
    public ReceviceMessageParams(int page, int pageSize, String phoneNo) {
        this.page = page;
        this.pageSize = pageSize;
        this.phoneNo = phoneNo;
    }

    public ReceviceMessageParams(int page, int pageSize, String phoneNo, String msgNature, String msgType) {
        this.page = page;
        this.pageSize = pageSize;
        this.phoneNo = phoneNo;
        this.msgNature = msgNature;
        this.msgType = msgType;
    }
}
