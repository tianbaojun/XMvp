package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 当前有效校历的配置信息
 */
public class SchoolCalendarManagerTime implements Serializable{

    public String method;

    public SchoolCalendarManagerTime(String method) {
        this.method = method;
    }
}
