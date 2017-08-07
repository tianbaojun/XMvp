package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/28.
 */
public class SendMessageListParams implements Serializable{
    private int page;
    private int pageSize;
    private String  msgType;

    public SendMessageListParams(int page, int pageSize, String msgType) {
        this.page = page;
        this.pageSize = pageSize;
        this.msgType = msgType;
    }
}
