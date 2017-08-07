package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 获取周的数据
 * Created by Administrator on 2016/6/29.
 */
public class WeekNumDataParams implements Serializable{
    private String week;
    private String dutyId;

    public WeekNumDataParams(String week, String dutyId) {
        this.week = week;
        this.dutyId = dutyId;
    }
    //
//    public WeekNumDataParams(String weekNum) {
//        this.week = weekNum;
//    }
}
