package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 回复新闻
 * Created by Administrator on 2016/6/16.
 */
public class ReplayNewsBean implements Serializable{
    private String newsId;
    private String  msgContect;

    public ReplayNewsBean(String newsId, String replyMsg) {
        this.newsId = newsId;
        this.msgContect = replyMsg;
    }
}
