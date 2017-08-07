package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 新闻详情
 * Created by Administrator on 2016/6/16.
 */
public class NewsDetailParams implements Serializable{
    private String newsId;

    public NewsDetailParams(String newsId) {
        this.newsId = newsId;
    }
}
