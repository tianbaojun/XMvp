package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/24.
 */
public class PraiseParamTrueFood implements Serializable{
    private String cookbookId;
    private boolean praiseFlag;

    public PraiseParamTrueFood(boolean praiseFlag,String cookbookId ) {
        this.cookbookId = cookbookId;
        this.praiseFlag = praiseFlag;
    }
}
