package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/19.
 */
public class LeaveApplyForStateRequest implements Serializable {

    public String oid;
    private boolean isPageFlag;
    private int currPage;
    private int pageSize;

    public LeaveApplyForStateRequest(String oid, boolean isPageFlag, int currPage, int pageSize) {
        this.oid = oid;
        this.isPageFlag = isPageFlag;
        this.currPage = currPage;
        this.pageSize = pageSize;
    }

    public LeaveApplyForStateRequest(String oid) {
        this.oid = oid;
    }
}
