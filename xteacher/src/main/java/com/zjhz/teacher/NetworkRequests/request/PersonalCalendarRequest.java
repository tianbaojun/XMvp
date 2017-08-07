package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;
/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 个人行事历
 */
public class PersonalCalendarRequest implements Serializable {

    public String scheduleTime;

    public PersonalCalendarRequest(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
}
