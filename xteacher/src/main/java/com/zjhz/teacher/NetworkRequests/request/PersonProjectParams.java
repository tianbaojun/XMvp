package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/13.
 */
public class PersonProjectParams implements Serializable {
    public String inspectType;  // 1个人 2班级
    public String status;   // 0保存 1启用
    public String page;
    public String pageSize;

    public PersonProjectParams(String inspectType, String status, String page, String pageSize) {
        this.inspectType = inspectType;
        this.status = status;
        this.page = page;
        this.pageSize = pageSize;
    }

    /**获取项目用这个*/
    public PersonProjectParams(String inspectType, String status) {
        this.inspectType = inspectType;
        this.status = status;
    }

    /**获取班级德育方案用这个*/
    public PersonProjectParams(String status) {
        this.status = status;
    }
}
