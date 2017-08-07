package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by zzd on 2017/7/13.
 */

public class XKHistoryListBean implements Serializable {
    /**
     * studentId : 288114322905763848
     * classId : 288113366604451840
     * level : 0
     * courseNo : 6S002
     * name : 汤嵩
     * className : 一（1）班
     * subjectId : 288073719195111424
     * status : 2
     * subjectName : 美术
     */

    private String studentId;
    private String classId;
    private int level;
    private String courseNo;
    private String name;
    private String className;
    private String subjectId;
    private int status;
    private String subjectName;

    private String statusName;
    private String levelName;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStatusName() {
        switch (status){
            case 0:
                statusName = "出勤";
                break;
            case 1:
                statusName = "迟到";
                break;
            case 2:
                statusName = "缺勤";
                break;
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getLevelName() {
        switch (level){
            case 0:
                levelName = "优";
                break;
            case 1:
                levelName = "良";
                break;
            case 2:
                levelName = "中";
                break;
            case 3:
                levelName = "一般";
                break;
            case 4:
                levelName = "差";
                break;
        }
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
