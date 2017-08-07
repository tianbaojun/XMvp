package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 我的报修列表
 * Created by Administrator on 2016/7/18.
 */
public class MyRepairsParams implements Serializable{
    private int page;
    private int pageSize;
    private String startTime;
    private String endTime;
    private String status;

    public MyRepairsParams(int page, int pageSize, String startTime, String endTime) {
        this.page = page;
        this.pageSize = pageSize;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public MyRepairsParams(int page, int pageSize, String startTime, String endTime,String status) {
        this.page = page;
        this.pageSize = pageSize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
