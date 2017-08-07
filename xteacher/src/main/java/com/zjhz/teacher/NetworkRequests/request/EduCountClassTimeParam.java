package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/8.
 */

public class EduCountClassTimeParam implements Serializable {
    private String startTime,endTime,classId;

    public EduCountClassTimeParam(String startTime, String endTime, String classId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.classId = classId;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
