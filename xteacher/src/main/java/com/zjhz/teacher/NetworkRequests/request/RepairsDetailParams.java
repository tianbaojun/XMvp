package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/14.
 */
public class RepairsDetailParams implements Serializable{
    private String repairId;
    private boolean isPageFlag;
    private int currPage;
    private int pageSize;

    public RepairsDetailParams(String repairId, boolean isPage, int page, int pageSize) {
        this.repairId = repairId;
        this.isPageFlag = isPage;
        this.currPage = page;
        this.pageSize = pageSize;
    }
}
