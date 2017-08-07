package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by zzd on 2017/7/15.
 */

public class HomeworkSearchReqBean implements Serializable{
    public String page;
    public String pageSize;
    public String status;
    public String subjectId;
    public String startTime;
    public String endTime;
}
