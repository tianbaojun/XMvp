package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 个人中心
 * Created by Administrator on 2016/8/1.
 */
public class CenterParams implements Serializable{
    private String teacherId = "";
    private int page;
    private int pageSize;

    public CenterParams(String teacherId, int page, int pageSize) {
        this.teacherId = teacherId;
        this.page = page;
        this.pageSize = pageSize;
    }

    public CenterParams(String teacherId) {
        this.teacherId = teacherId;
    }
}
