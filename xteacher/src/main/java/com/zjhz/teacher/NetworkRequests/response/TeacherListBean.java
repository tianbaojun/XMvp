package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * 每日值日的人及领导
 * Created by Administrator on 2016/6/29.
 */
public class TeacherListBean implements Serializable{
    private String week = "";
    private String dutyTime = "";
    private String content = "";
    private boolean hasAffairs = false;
    private List<TeacherBean> wleaders ;
    private List<TeacherBean> teachers ;
    private List<TeacherBean> leaders ;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasAffairs() {
        return hasAffairs;
    }

    public void setHasAffairs(boolean hasAffairs) {
        this.hasAffairs = hasAffairs;
    }

    public String getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(String dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<TeacherBean> getWleaders() {
        return wleaders;
    }

    public void setWleaders(List<TeacherBean> wleaders) {
        this.wleaders = wleaders;
    }

    public List<TeacherBean> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherBean> teachers) {
        this.teachers = teachers;
    }

    public List<TeacherBean> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<TeacherBean> leaders) {
        this.leaders = leaders;
    }
}
