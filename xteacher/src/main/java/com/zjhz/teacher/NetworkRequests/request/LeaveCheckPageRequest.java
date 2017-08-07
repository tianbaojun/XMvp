package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/17.
 */
public class LeaveCheckPageRequest implements Serializable {
    public String page;
    public String pageSize;
    public String oid;  // 请假的oid

    public LeaveCheckPageRequest(String page, String pageSize, String oid) {
        this.page = page;
        this.pageSize = pageSize;
        this.oid = oid;
    }
}
