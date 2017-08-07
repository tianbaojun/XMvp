package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ClassAndGradeEducationListCheckScore implements Serializable {
    public String gradeId;
    public String schemeId;
    public String page;
    public String pageSize;
    public String checkTime;
    public String studentId;

    public ClassAndGradeEducationListCheckScore(String gradeId, String schemeId, String page, String pageSize) {
        this.gradeId = gradeId;
        this.schemeId = schemeId;
        this.page = page;
        this.pageSize = pageSize;
    }

    public ClassAndGradeEducationListCheckScore(String gradeId, String schemeId, String checkTimes ,String studentId, String page, String pageSize) {
        this.gradeId = gradeId;
        this.schemeId = schemeId;
        this.page = page;
        this.pageSize = pageSize;
        this.checkTime = checkTimes;
        this.studentId = studentId;
    }
}
