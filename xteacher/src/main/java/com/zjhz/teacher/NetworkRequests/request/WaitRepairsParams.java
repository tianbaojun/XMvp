package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 审批报修
 * Created by Administrator on 2016/7/19.
 */
public class WaitRepairsParams implements Serializable{
    public String startTime;
    public String endTime;
    public String status;
    public WaitRepairsParams(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public WaitRepairsParams(String startTime, String endTime,String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
