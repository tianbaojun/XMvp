package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 获取所有个人行事历信息
 */
public class PersonalCalendarAllRequest implements Serializable {

    public String scheduleTime; // 当月的时间

    public PersonalCalendarAllRequest(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
}
