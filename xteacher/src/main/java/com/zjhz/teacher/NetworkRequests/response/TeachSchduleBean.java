package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 教师课表
 * Created by Administrator on 2016/7/5.
 */
public class TeachSchduleBean implements Serializable{
    private String gradeName = "";
    private String className = "";
    private String subjectName = "";
    private String week ="";
    private String clazz ="";
    //课表当中的第几节，在arrays的scheduledata当中以21形式代表，2代表周一，1代表周一第二节课
    private String flag = "";

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
