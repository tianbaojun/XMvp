package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 作业检索
 * Created by Administrator on 2016/6/28.
 */
public class RetrieveParams implements Serializable{
    private String classId;
    private String subjectId;
    private String startTime;
    private String endTime;
    private int page;
    private int pageSize;
    private String teacherId;

    public RetrieveParams(String classId, String subjectId, String startTime, String endTime, int page, int pageSize, String teacherId) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.page = page;
        this.pageSize = pageSize;
        this.teacherId = teacherId;
    }

    public RetrieveParams(String classId, String subjectId, String startTime, String endTime, int page, int pageSize) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.page = page;
        this.pageSize = pageSize;
    }

}
