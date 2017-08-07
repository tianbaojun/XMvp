package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SchoolLocationBean implements Serializable{
    private String pointxBaidu;
    private String pointyBaidu;
    private String stationAddress;
    private String deviceType;
    private String stationCode;
    private String recordTime;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getPointxBaidu() {
        return pointxBaidu;
    }

    public void setPointxBaidu(String pointxBaidu) {
        this.pointxBaidu = pointxBaidu;
    }

    public String getPointyBaidu() {
        return pointyBaidu;
    }

    public void setPointyBaidu(String pointyBaidu) {
        this.pointyBaidu = pointyBaidu;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
