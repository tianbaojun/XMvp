package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 修改个人行事历信息
 */
public class PersonalCalendarModifyRequest implements Serializable {

    public String calendarId; // 日程ID
    public String priority; // 事件优先级，优先级等级分为高（红色）、中（黄色）、低（蓝色）、无（灰色），默认普通日程优先级为无，值日日程优先级为中，默认为无
    public String title; // 标题内容
    public String summary;  // 事件备注
    public String scheduleType;  //
    public String scheduleTime;  //

    public PersonalCalendarModifyRequest(String scheduleTime,String scheduleType,String calendarId, String priority, String title, String summary) {
        this.scheduleTime = scheduleTime;
        this.scheduleType = scheduleType;
        this.calendarId = calendarId;
        this.priority = priority;
        this.title = title;
        this.summary = summary;
    }
}
