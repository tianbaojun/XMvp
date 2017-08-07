package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 学生当前位置查询   根据班级年级查询学生列表
 */
public class GradeClassRequest implements Serializable{

    public String page;
    public String pageSize;
    public String gradeId;
    public String classId;

    public GradeClassRequest(String page, String pageSize, String gradeId, String classId) {
        this.page = page;
        this.pageSize = pageSize;
        this.gradeId = gradeId;
        this.classId = classId;
    }
}
