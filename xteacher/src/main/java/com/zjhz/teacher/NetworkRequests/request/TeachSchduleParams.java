package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/5.
 */
public class TeachSchduleParams implements Serializable{
    private String teacherId;
    private int page;
    private int pageSize;

    public TeachSchduleParams(String teacherId, int page, int pageSize) {
        this.teacherId = teacherId;
        this.page = page;
        this.pageSize = pageSize;
    }

    public TeachSchduleParams(String teacherId) {
        this.teacherId = teacherId;
    }
}
