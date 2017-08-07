package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/9.
 */
public class PersonEduSpecialParam implements Serializable{
    private int status;
    public String page;
    public String pageSize;

    public PersonEduSpecialParam(int status) {
        this.status = status;
    }

    public PersonEduSpecialParam(int status, String page, String pageSize) {
        this.status = status;
        this.page = page;
        this.pageSize = pageSize;
    }
}
