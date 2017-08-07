package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表
 */
public class LeaveApplyForList implements Serializable {

    public String name;
    public String type;
    public String content;
    public String time;
    public String state;
    public String oid;    // 请假单ID
    public int curNode;    // 0 驳回
    public String approveFlag;    // 1是可审批，2是非审批
    public String flowStatus;    // 1是审批通过

    public String startTime;  // 起始时间
    public int sma;  // 起始时间上下午
    public String endTime;    // 截止时间
    public int ema;    // 截止时间上下午
    public String appType;    // 请假类型
    public String reason;     // 请假理由
    public String photoUrl;     // 请假理由
}
