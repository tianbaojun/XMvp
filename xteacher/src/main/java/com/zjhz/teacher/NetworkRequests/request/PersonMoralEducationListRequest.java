package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/29.
 */
public class PersonMoralEducationListRequest implements Serializable {

    public String page;
    public String pageSize;
    public String moralManId;      // 详情
    public String certificateId;   // 学号
    public String classId;
    public String gradeId;
    public String moralId;  // 德育项目
    public String startTime;  // 起始时间
    public String endTime;  // 截止时间

    public PersonMoralEducationListRequest(String endTime, String startTime, String page, String pageSize, String classId, String moralID) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.page = page;
        this.pageSize = pageSize;
        this.classId = classId;
        this.moralId = moralID;
    }

    public PersonMoralEducationListRequest(String moralManId) {
        this.moralManId = moralManId;
    }

    public PersonMoralEducationListRequest(String pageSize, String page) {
        this.pageSize = pageSize;
        this.page = page;
    }

    public PersonMoralEducationListRequest(String pageSize, int page,String classId) {
        this.pageSize = pageSize;
        this.page = page + "";
        this.classId = classId;
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param certificateId  学号
     */
    public PersonMoralEducationListRequest(String page, String pageSize, String certificateId) {
        this.page = page;
        this.pageSize = pageSize;
        this.certificateId = certificateId;
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param gradeId  年级
     * @param classId  班级
     */
    public PersonMoralEducationListRequest(String page, String pageSize, String gradeId, String classId) {
        this.page = page;
        this.pageSize = pageSize;
        this.gradeId = gradeId;
        this.classId = classId;
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param moralID  德育项目
     * @param gradeId  年级
     * @param classId  班级
     */
    public PersonMoralEducationListRequest(String page, String pageSize, String moralID, String gradeId, String classId) {
        this.page = page;
        this.pageSize = pageSize;
        this.moralId = moralID;
        this.gradeId = gradeId;
        this.classId = classId;
    }


}
