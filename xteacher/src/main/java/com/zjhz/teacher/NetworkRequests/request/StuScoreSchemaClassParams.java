package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class StuScoreSchemaClassParams implements Serializable{
    private int page;
    private int pageSize ;
    private String year ;
    private String semester ;
    private String stuscoreType ;
    private String classId ;
    private String subjectId ;
    private String startTime ;
    private String endTime ;
    private String status ;

    public StuScoreSchemaClassParams(int page, int pageSize, String status) {
        this.page = page;
        this.pageSize = pageSize;
        this.status = status;
    }

    public StuScoreSchemaClassParams(int page, int pageSize, String year, String semester, String stuscoreType, String classId, String subjectId, String startTime, String endTime, String status) {
        this.page = page;
        this.pageSize = pageSize;
        this.year = year;
        this.semester = semester;
        this.stuscoreType = stuscoreType;
        this.classId = classId;
        this.subjectId = subjectId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
