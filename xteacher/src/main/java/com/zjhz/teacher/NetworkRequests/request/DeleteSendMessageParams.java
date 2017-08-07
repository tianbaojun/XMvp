package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/24.
 */
public class DeleteSendMessageParams implements Serializable{
    private String msgId;

    public DeleteSendMessageParams(String msgId) {
        this.msgId = msgId;
    }
}
