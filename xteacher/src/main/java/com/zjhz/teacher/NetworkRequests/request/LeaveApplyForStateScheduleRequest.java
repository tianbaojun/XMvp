package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/19.
 * 请假详情课表查询  排课列表
 */
public class LeaveApplyForStateScheduleRequest implements Serializable {

    public String teacherId;
    public String startTime;
    public String endTime;
    public int page;
    public int pageSize;

    public LeaveApplyForStateScheduleRequest(String teacherId, String startTime, String endTime, int page, int pageSize) {
        this.teacherId = teacherId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.page = page;
        this.pageSize = pageSize;
    }
}
