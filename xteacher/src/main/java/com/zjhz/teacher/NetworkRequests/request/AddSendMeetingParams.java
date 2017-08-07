package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 会议通知   合并群发接口2016/10/14.
 * Created by Administrator on 2016/7/19.
 */
public class AddSendMeetingParams implements Serializable{
   /* private String msgTypes;
    private String meetingContent;
    private String userIds;
    private String phones;
    private String meetingTime;
    private String meetingTitle;

    public AddSendMeetingParams(String msgTypes, String meetingContent, String userIds, String phones, String meetingTime, String meetingTitle) {
        this.msgTypes = msgTypes;
        this.meetingContent = meetingContent;
        this.userIds = userIds;
        this.phones = phones;
        this.meetingTime = meetingTime;
        this.meetingTitle = meetingTitle;
    }*/

    private String msgTypes;
    private String content;
    private String userIds;
    private String phones;
    private String eventTime;
    private String alert;
    private String msgNature;
    private int page;
    private int pageSize;
    public AddSendMeetingParams(String msgTypes, String content, String userIds, String phones, String eventTime, String alert, String msgNature) {
        this.msgTypes = msgTypes;
        this.content = content;
        this.userIds = userIds;
        this.phones = phones;
        this.eventTime = eventTime;
        this.alert = alert;
        this.msgNature = msgNature;
    }

    public AddSendMeetingParams(int page, int pageSize, String msgNature) {
        this.page = page;
        this.pageSize = pageSize;
        this.msgNature = msgNature;

    }
}
