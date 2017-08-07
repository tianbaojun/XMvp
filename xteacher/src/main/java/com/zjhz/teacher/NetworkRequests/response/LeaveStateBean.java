package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class LeaveStateBean implements Serializable{
    private String week = "";
    private String clazz = "";
    private String linkTime = "";
    private String linkId = "";
    private String subjectName = "";
    private String className = "";
    private String oname = "";
    private int knowsStatus; //了解状态 0 不了解  1 了解

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

    public String getLinkTime() {
        return linkTime;
    }

    public void setLinkTime(String linkTime) {
        this.linkTime = linkTime;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public int getKnowsStatus() {
        return knowsStatus;
    }

    public void setKnowsStatus(int knowsStatus) {
        this.knowsStatus = knowsStatus;
    }
}
