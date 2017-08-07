package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ClassAndGradeEducationListParams implements Serializable {
    public String schemeName;  // 德育方案
    public String checkTime;   // 检查时间
    public String startTime;
    public String endTime;
    public String gradeId;   // 年级
    public String page;
    public String pageSize;

    public ClassAndGradeEducationListParams(String pageSize, String page) {
        this.pageSize = pageSize;
        this.page = page;
    }

    public ClassAndGradeEducationListParams(String schemeName, String startTime, String endTime, String gradeId, String page, String pageSize) {
        this.schemeName = schemeName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gradeId = gradeId;
        this.page = page;
        this.pageSize = pageSize;
    }
}
