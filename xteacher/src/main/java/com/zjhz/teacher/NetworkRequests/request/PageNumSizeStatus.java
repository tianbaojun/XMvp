package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/13.
 */
public class PageNumSizeStatus implements Serializable {
    private int page;
    private int pageSize;
    private int status;
    private String teacherId;

    public PageNumSizeStatus(int page, int pageSize, int status, String teacherId) {
        this.page = page;
        this.pageSize = pageSize;
        this.status = status;
        this.teacherId = teacherId;
    }

   /* public PageNumSizeStatus(int page, int pageSize, int status) {
        this.page = page;
        this.pageSize = pageSize;
        this.status = status;
    }*/
}
