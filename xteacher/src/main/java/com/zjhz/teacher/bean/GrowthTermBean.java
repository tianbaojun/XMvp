package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/13.
 * Description:
 * What Changed:
 */

public class GrowthTermBean implements Serializable {


    /**
     * createTime : 2017-06-13 16:43:39
     * createUser : 288114334976970768
     * endTime : 2017-06-11 00:00:00
     * id : 392120972393582592
     * schoolId : 288069341826519040
     * semester : SCHOOL_CALENDAR_SEMESTER_1
     * semesterVal : 第一学期
     * startTime : 2017-02-11 00:00:00
     * title : 2015-2016第一学期
     * updateTime : 2017-06-13 17:18:28
     * year : SCHOOL_CALENDAR_YEAR_1
     * yearVal : 2015-2016
     */

    private String createTime;
    private String createUser;
    private String endTime;
    private String id;
    private String schoolId;
    private String semester;
    private String semesterVal;
    private String startTime;
    private String title;
    private String updateTime;
    private String year;
    private String yearVal;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSemesterVal() {
        return semesterVal;
    }

    public void setSemesterVal(String semesterVal) {
        this.semesterVal = semesterVal;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYearVal() {
        return yearVal;
    }

    public void setYearVal(String yearVal) {
        this.yearVal = yearVal;
    }
}
