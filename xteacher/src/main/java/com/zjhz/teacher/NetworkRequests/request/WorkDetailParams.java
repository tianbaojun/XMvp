package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 作业详情
 * Created by Administrator on 2016/6/25.
 */
public class WorkDetailParams implements Serializable{

    private String homeworkId;

    public WorkDetailParams(String homeworkId) {
        this.homeworkId = homeworkId;
    }
}
