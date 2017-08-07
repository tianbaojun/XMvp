package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/12.
 */
public class SupplyTeacherRequest implements Serializable {

    public String startTime;
    public String endTime;
    public String teacherId;

    public SupplyTeacherRequest(String teacherId,String startTime, String endTime) {
        this.teacherId = teacherId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
