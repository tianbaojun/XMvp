package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 评论食物列表
 * Created by Administrator on 2016/6/24.
 */
public class ReplayFoodBean implements Serializable{
    private String cookbookId;
    private String msgContect;

    public ReplayFoodBean(String cookbookId, String msgContect) {
        this.cookbookId = cookbookId;
        this.msgContect = msgContect;
    }
}
