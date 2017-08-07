package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 群发消息
 * Created by Administrator on 2016/7/22.
 */
public class AddSendMessageParams implements Serializable{
    private String msgTypes;
    private String content;
    private String userIds;
    private String phones;
    private String alert;
    private String msgNature;

    public AddSendMessageParams(String msgTypes, String content, String userIds, String phones, String alert, String msgNature) {
        this.msgTypes = msgTypes;
        this.content = content;
        this.userIds = userIds;
        this.phones = phones;
        this.alert = alert;
        this.msgNature = msgNature;
    }
}
