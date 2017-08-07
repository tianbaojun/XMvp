package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/17.
 */
public class LeaveTipsayAddRequest implements Serializable {
    public String schoolId;  // 所在学校
    public String oid;  // 请假的oid
    public String oteacherId;  // 请假老师的id
    public String oteacher_id;

    // 否
    public String week;  // 星期
    public String clazz;  // 节次
    public String subjectId;  // 科目id
    public String lteacherId;  // 代课老师的id
    public String classId;  // 班级的id
    public String linkTime;  // 对应的时间

    //修改代课
    public String  linkId;
    public int groupChild;

    public LeaveTipsayAddRequest(String linkId, String clazz, String week, String lteacherId, String subjectId) {
        this.linkId = linkId;
        this.clazz = clazz;
        this.week = week;
        this.lteacherId = lteacherId;
        this.subjectId = subjectId;
    }

    /**
     * @param schoolId   所在学校
     * @param oid        请假的oid
     * @param oteacherId 请假老师的id
     * @param week       星期
     * @param clazz      节次
     * @param subjectId  科目id
     * @param lteacherId  代课老师的id
     */
    public LeaveTipsayAddRequest(String linkTime,String classId,String schoolId, String oid, String oteacherId, String week, String clazz, String subjectId, String lteacherId) {
        this.linkTime = linkTime;
        this.classId = classId;
        this.schoolId = schoolId;
        this.oid = oid;
        this.oteacherId = oteacherId;
        this.week = week;
        this.clazz = clazz;
        this.subjectId = subjectId;
        this.lteacherId = lteacherId;
    }
    /**
     * @param schoolId   所在学校
     * @param oid        请假的oid
     * @param oteacherId 请假老师的id
     * @param week       星期
     * @param clazz      节次
     * @param subjectId  科目id
     * @param lteacherId  代课老师的id
     */
    public LeaveTipsayAddRequest(String linkTime,String classId,String schoolId, String oid, String oteacherId, String week, String clazz, String subjectId, String lteacherId, String linkId) {
        this.linkTime = linkTime;
        this.classId = classId;
        this.schoolId = schoolId;
        this.oid = oid;
        this.oteacherId = oteacherId;
        this.oteacher_id = oteacherId;
        this.week = week;
        this.clazz = clazz;
        this.subjectId = subjectId;
        this.lteacherId = lteacherId;
        this.linkId = linkId;
    }
}
