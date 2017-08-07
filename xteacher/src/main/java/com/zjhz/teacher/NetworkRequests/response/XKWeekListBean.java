package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by zzd on 2017/7/13.
 */

public class XKWeekListBean implements Serializable {
    /**
     * week : 第1周
     * index : 1
     * startTime : 2017-02-01 00:00:00
     * endTime : 2017-02-05 23:59:59
     */

    private String week;
    private int index;
    private String startTime;
    private String endTime;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
