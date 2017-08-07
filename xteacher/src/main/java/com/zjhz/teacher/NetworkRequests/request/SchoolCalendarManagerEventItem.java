package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 当前有效校历的配置信息
 */
public class SchoolCalendarManagerEventItem implements Serializable {

    public String weekNum;  // 周次
    public String page;
    public String pageSize;

    public SchoolCalendarManagerEventItem(String weekNum, String page, String pageSize) {
        this.weekNum = weekNum;
        this.page = page;
        this.pageSize = pageSize;
    }
}
