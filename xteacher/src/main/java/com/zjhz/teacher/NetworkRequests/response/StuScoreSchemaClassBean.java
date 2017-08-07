package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class StuScoreSchemaClassBean implements Serializable{
    private String createUser;
    private String createUserName;
    private String createTime;
    private String classId;
    private String stuscoreClassId;
    private String yearVal;
    private String semesterVal;
    private String stuscoreTypeVal;
    private String subjectName;
    private String className;
    private String scoreMethod;

    public String getScoreMethod() {
        return scoreMethod;
    }

    public void setScoreMethod(String scoreMethod) {
        this.scoreMethod = scoreMethod;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getYearVal() {
        return yearVal;
    }

    public void setYearVal(String yearVal) {
        this.yearVal = yearVal;
    }

    public String getSemesterVal() {
        return semesterVal;
    }

    public void setSemesterVal(String semesterVal) {
        this.semesterVal = semesterVal;
    }

    public String getStuscoreClassId() {
        return stuscoreClassId;
    }

    public void setStuscoreClassId(String stuscoreClassId) {
        this.stuscoreClassId = stuscoreClassId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStuscoreTypeVal() {
        return stuscoreTypeVal;
    }

    public void setStuscoreTypeVal(String stuscoreTypeVal) {
        this.stuscoreTypeVal = stuscoreTypeVal;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
