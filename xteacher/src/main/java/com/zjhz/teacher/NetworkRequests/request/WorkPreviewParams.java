package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 作业详情
 * Created by Administrator on 2016/6/25.
 */
public class WorkPreviewParams implements Serializable{

    public String page;
    public String pageSize;
    public String homeworkId;

    public WorkPreviewParams(String page, String pageSize, String homeworkId) {
        this.page = page;
        this.pageSize = pageSize;
        this.homeworkId = homeworkId;
    }
}
