package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class SchoolRfidParams implements Serializable{
    private String gradeId;
    private String classId;
    private int page;
    private int pageSize;
    public SchoolRfidParams(String gradeId, String classId) {
        this.gradeId = gradeId;
        this.classId = classId;
        this.page = 1;
        this.pageSize = 120;
    }
}
