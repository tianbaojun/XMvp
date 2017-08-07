package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by zzd on 2017/5/3.
 */

public class VisitorListBean implements Serializable {

    /**
     * page : 1
     * pageSize : 10
     * teacherName : 冯素娟
     * name : 李丽娜
     * appointmentTimeStart : 2017-04-01 09:00
     * appointmentTimeEnd : 2017-05-27 12:00
     * realTimeStart : 2017-04-01 09:00
     * realTimeEnd : 2017-05-27 12:00
     */

    private int page;
    private int pageSize;
    private String teacherId;
    private String teacherName;
    private String visitorName;
    private String appointmentTimeStart;
    private String appointmentTimeEnd;
    private String realTimeStart;
    private String realTimeEnd;
    private String visitStatus;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getAppointmentTimeStart() {
        return appointmentTimeStart;
    }

    public void setAppointmentTimeStart(String appointmentTimeStart) {
        this.appointmentTimeStart = appointmentTimeStart;
    }

    public String getAppointmentTimeEnd() {
        return appointmentTimeEnd;
    }

    public void setAppointmentTimeEnd(String appointmentTimeEnd) {
        this.appointmentTimeEnd = appointmentTimeEnd;
    }

    public String getRealTimeStart() {
        return realTimeStart;
    }

    public void setRealTimeStart(String realTimeStart) {
        this.realTimeStart = realTimeStart;
    }

    public String getRealTimeEnd() {
        return realTimeEnd;
    }

    public void setRealTimeEnd(String realTimeEnd) {
        this.realTimeEnd = realTimeEnd;
    }

    public String getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        this.visitStatus = visitStatus;
    }
}
