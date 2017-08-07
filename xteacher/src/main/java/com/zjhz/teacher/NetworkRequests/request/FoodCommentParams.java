package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 食物评论
 * Created by Administrator on 2016/6/24.
 */
public class FoodCommentParams implements Serializable{
    private String cookbookId;
    private int page;
    private int pageSize;

    public FoodCommentParams(String cookbookId, int page, int pageSize) {
        this.cookbookId = cookbookId;
        this.page = page;
        this.pageSize = pageSize;
    }
}
