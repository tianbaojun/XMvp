package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表
 */
public class LeaveStateScheduleBean implements Serializable {

    public String clazz;  // 节次
    public String subject;  // 科目
    public String className;  // 班级
    public String classId;  // 班级Id
    public String tipsay;  // 代课

    public String schoolId;  // 学校id
    public String teacherId;  // 请假老师id
    public String week;  // 星期几
    public String subjectId;  // 科目Id
    public String linkId;
    public String oteacherId;
    public String lteacherId;
    public int knowsStatus = -1;//了解状态 0 不了解  1 了解
}
