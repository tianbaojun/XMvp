package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/8.
 */
public class DeleteNewsCommentParams implements Serializable{
    private String replyId;
    private String newsId;

    public DeleteNewsCommentParams(String replyId, String newsId) {
        this.replyId = replyId;
        this.newsId = newsId;
    }
}
