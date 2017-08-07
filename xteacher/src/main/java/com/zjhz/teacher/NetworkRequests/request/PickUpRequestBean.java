package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by zzd on 2017/5/16.
 */

public class PickUpRequestBean implements Serializable {

    public int page;
    public int pageSize;

    public String __curentUser; //当前用户ID（参数为系统自带）
    public String schoolId;	//当前学校ID（参数为系统自带）
    //0 待审批 1 已审批
    public int isApprove;
    public String term;	//学期
    public String realityShuttleStart;	//实际接送起始日期
    public String realityShuttleEnd;	//实际接送结束日期
    public String applyShuttleStart;	//申请接送起始日期
    public String applyShuttleEnd;	//申请接送结束日期
    public String studentClassName;	//学生班级

}
