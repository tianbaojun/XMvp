package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 学生当前位置查询   根据学生姓名和学籍号码查询学生信息
 */
public class EduStudentNameRequest implements Serializable {

    public String studentName;
    public String certificateId;

    public EduStudentNameRequest(String studentName, String certificateId) {
        this.studentName = studentName;
        this.certificateId = certificateId;
    }
//
//    public StudentNameRequest(String name) {
//        this.name = name;
//    }
}
