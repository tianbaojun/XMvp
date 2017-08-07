package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 新闻列表
 * Created by Administrator on 2016/6/22.
 */
public class NewsAndAnnouncementListParams implements Serializable{
    private String categoryId;
    private int status;
    private int page;
    private int pageSize;

    public NewsAndAnnouncementListParams(String categoryId, int status, int page, int pageSize) {
        this.categoryId = categoryId;
        this.status = status;
        this.page = page;
        this.pageSize = pageSize;
    }
}
