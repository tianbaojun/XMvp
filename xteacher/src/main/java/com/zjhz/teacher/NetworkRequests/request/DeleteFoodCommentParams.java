package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 删除评论
 * Created by Administrator on 2016/8/3.
 */
public class DeleteFoodCommentParams implements Serializable{
    private String replyId;
    private String cookbookId;

    public DeleteFoodCommentParams(String replyId, String cookbookId) {
        this.replyId = replyId;
        this.cookbookId = cookbookId;
    }
}
