package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class EduCountParams implements Serializable {
    private String startTime,endTime,gradeIds,sortType,reverseFlag,page,pageSize = "20";

    public void setPage(String page) {
        this.page = page;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public EduCountParams() {
    }

    public EduCountParams(String startTime, String endTime, String gradeId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.gradeIds = gradeId;
    }

    public EduCountParams(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EduCountParams(String startTime, String endTime, String gradeId, String sortType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.gradeIds = gradeId;
        this.sortType = sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public void setReverseFlag(String reverseFlag) {
        this.reverseFlag = reverseFlag;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setGradeIds(String gradeIds) {
        this.gradeIds = gradeIds;
    }
}
