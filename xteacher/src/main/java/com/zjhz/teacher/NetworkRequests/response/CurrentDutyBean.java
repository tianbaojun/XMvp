package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 当前值日值周的配置信息
 * Created by Administrator on 2016/6/29.
 */
public class CurrentDutyBean implements Serializable{
    private String startTime = "";
    private String endTime= "";
    private String dutyId = "";

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
