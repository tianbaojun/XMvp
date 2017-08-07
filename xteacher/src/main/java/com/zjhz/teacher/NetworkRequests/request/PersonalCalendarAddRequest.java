package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 添加个人行事历信息
 */
public class PersonalCalendarAddRequest implements Serializable {

    public String scheduleType; // 0:普通日程，1：值日信息，默认为0普通类型
    public String priority; // 事件优先级，优先级等级分为高（红色）、中（黄色）、低（蓝色）、无（灰色），默认普通日程优先级为无，值日日程优先级为中，默认为无
    public String scheduleTime;  // 当前日程记录日期
    public String title; // 标题内容

    public PersonalCalendarAddRequest(String scheduleType, String priority, String scheduleTime, String title) {
        this.scheduleType = scheduleType;
        this.priority = priority;
        this.scheduleTime = scheduleTime;
        this.title = title;
    }
}
