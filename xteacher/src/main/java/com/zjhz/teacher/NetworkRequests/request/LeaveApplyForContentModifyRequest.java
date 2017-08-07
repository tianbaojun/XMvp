package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请内容 请求参数
 */
public class LeaveApplyForContentModifyRequest implements Serializable {

    public String oid;  // 请假单ID
    public String schoolId;  // 所在学校
    public String applyer;  // 请假用户信息
    public String applyerTime;  // 申请日期 当前时间
    public String startTime;  // 请假开始时间
    public String sma;  // 请假开始时间    上午|下午）1,上午，2 下午
    public String endTime;  // 请假截止时间
    public String ema;  // 请假截止时间    上午|下午）1,上午，2 下午
    public String type;  // 请假类型:1	婚假,2	病假,3	事假
    public String summary;  // 请假详细描述
    public String week;  // 星期,与代课信息做关联
    public String clazz;  // 节次
    public String subjectId;  // 科目ID
    public String teacherId;  // 代课教师ID
    public String linkIds;

    /**
     *
     * @param applyer       请假用户信息
     * @param applyerTime   申请日期 当前时间
     * @param startTime     请假开始时间
     * @param sma           请假开始时间    上午|下午）1,上午，2 下午
     * @param endTime       请假截止时间
     * @param ema           请假截止时间    上午|下午）1,上午，2 下午
     * @param type          请假类型:1	婚假,2	病假,3	事假
     * @param summary       请假详细描述
     */
    public LeaveApplyForContentModifyRequest(String oid,String schoolId, String applyer, String applyerTime, String startTime, String sma, String endTime, String ema, String type, String summary, String week, String clazz, String subjectId, String teacherId) {
        this.oid = oid;
        this.schoolId = schoolId;
        this.applyer = applyer;
        this.applyerTime = applyerTime;
        this.startTime = startTime;
        this.sma = sma;
        this.endTime = endTime;
        this.ema = ema;
        this.type = type;
        this.summary = summary;
        this.week = week;
        this.clazz = clazz;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
    }

    /**
     *
     * @param applyer       请假用户信息
     * @param applyerTime   申请日期 当前时间
     * @param startTime     请假开始时间
     * @param sma           请假开始时间    上午|下午）1,上午，2 下午
     * @param endTime       请假截止时间
     * @param ema           请假截止时间    上午|下午）1,上午，2 下午
     * @param type          请假类型:1	婚假,2	病假,3	事假
     * @param summary       请假详细描述
     */
    public LeaveApplyForContentModifyRequest(String oid,String schoolId, String applyer, String applyerTime, String startTime, String sma, String endTime, String ema, String type, String summary, String week, String clazz, String subjectId, String teacherId, String linkIds) {
        this.oid = oid;
        this.schoolId = schoolId;
        this.applyer = applyer;
        this.applyerTime = applyerTime;
        this.startTime = startTime;
        this.sma = sma;
        this.endTime = endTime;
        this.ema = ema;
        this.type = type;
        this.summary = summary;
        this.week = week;
        this.clazz = clazz;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.linkIds = linkIds;
    }
}
