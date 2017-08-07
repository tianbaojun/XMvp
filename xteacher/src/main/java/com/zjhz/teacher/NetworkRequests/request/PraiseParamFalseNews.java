package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 点赞接口
 * Created by xiangxue on 2016/6/17.
 */
public class PraiseParamFalseNews implements Serializable{
    private boolean praiseFlag;
    private String newsId;
    private String praiseId;

    public PraiseParamFalseNews(boolean praiseFlag, String newsId, String praiseId) {
        this.praiseFlag = praiseFlag;
        this.newsId = newsId;
        this.praiseId = praiseId;
    }
}
