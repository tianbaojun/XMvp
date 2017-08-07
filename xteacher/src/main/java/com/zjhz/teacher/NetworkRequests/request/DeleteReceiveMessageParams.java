package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 删除接收消息
 * Created by Administrator on 2016/8/24.
 */
public class DeleteReceiveMessageParams implements Serializable{
    private String linkId;
//    private String msgId;

    public DeleteReceiveMessageParams(String linkId) {
        this.linkId = linkId;
    }

//    public DeleteReceiveMessageParams(String linkId, String msgId) {
//        this.linkId = linkId;
//        this.msgId = msgId;
//    }
}
