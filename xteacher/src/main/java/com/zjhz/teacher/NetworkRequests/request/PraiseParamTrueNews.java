package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/17.
 */
public class PraiseParamTrueNews implements Serializable{
    private boolean praiseFlag;
    private String newsId;

    public PraiseParamTrueNews(boolean praiseFlag, String newsId) {
        this.praiseFlag = praiseFlag;
        this.newsId = newsId;
    }
}
