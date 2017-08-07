package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表查询教师名称分页
 */
public class MyScheduleQueryNamePaging implements Serializable {

    public String name;
    public String page;
    public String pageSize;

    public MyScheduleQueryNamePaging(String name, String page, String pageSize) {
        this.name = name;
        this.page = page;
        this.pageSize = pageSize;
    }
}
