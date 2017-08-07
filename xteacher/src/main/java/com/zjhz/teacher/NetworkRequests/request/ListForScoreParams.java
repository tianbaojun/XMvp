package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class ListForScoreParams implements Serializable{
    private String stuscoreClassId;
    private int page;
    private int pageSize;

    public ListForScoreParams(String stuscoreClassId, int page, int pageSize) {
        this.stuscoreClassId = stuscoreClassId;
        this.page = page;
        this.pageSize = pageSize;
    }

}
