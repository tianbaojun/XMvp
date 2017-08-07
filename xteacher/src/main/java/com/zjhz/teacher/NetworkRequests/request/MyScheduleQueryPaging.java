package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表查询教师进入分页
 */
public class MyScheduleQueryPaging implements Serializable {

    public String page;
    public String pageSize;

    public MyScheduleQueryPaging(String page, String pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
}
