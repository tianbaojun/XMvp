package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 考勤列表数据
 * Created by Administrator on 2016/7/6.
 */
public class AttendanceListBean implements Serializable{
    private String name;
    private String recordTime;
    private String stationAddress;
    private int function1;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public int getFunction1() {
        return function1;
    }

    public void setFunction1(int function1) {
        this.function1 = function1;
    }
}
