package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/24.
 */
public class PraiseParamFlaseFood implements Serializable{
    private boolean praiseFlag;
    private String cookbookId;
    private String praiseId;

    public PraiseParamFlaseFood(boolean praiseFlag, String cookbookId, String praiseId) {
        this.praiseFlag = praiseFlag;
        this.cookbookId = cookbookId;
        this.praiseId = praiseId;
    }
}
