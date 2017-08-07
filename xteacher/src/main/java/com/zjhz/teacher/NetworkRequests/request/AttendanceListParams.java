package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class AttendanceListParams implements Serializable{
    private String date;
    private String classId;
    private String function1;
    private String flag;
//    private String cardNums;
    private int page;
    private int pageSize;

    public AttendanceListParams(String date, String classId, String function1, String flag, int page, int pageSize) {
        this.date = date;
        this.classId = classId;
        this.function1 = function1;
        this.flag = flag;
        this.page = page;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "AttendanceListParams{" +
                "date='" + date + '\'' +
                ", classId='" + classId + '\'' +
                ", function1='" + function1 + '\'' +
                ", flag='" + flag + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
