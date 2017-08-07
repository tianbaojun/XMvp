package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/28.
 */
public class DeleteHomeworkParams implements Serializable{
    private String homeworkIds;

    public DeleteHomeworkParams(String homeworkIds) {
        this.homeworkIds = homeworkIds;
    }
}
