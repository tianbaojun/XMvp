package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 新闻评论
 * Created by Administrator on 2016/6/24.
 */
public class CommentParams implements Serializable {
    private String newsId;
    private int page;
    private int pageSize;

    public CommentParams(String newsId, int page, int pageSize) {
        this.newsId = newsId;
        this.page = page;
        this.pageSize = pageSize;
    }
}
