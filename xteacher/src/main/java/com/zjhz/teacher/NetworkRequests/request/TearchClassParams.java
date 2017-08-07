package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 获取班级
 * Created by Administrator on 2016/6/27.
 */
public class TearchClassParams implements Serializable{
    private String userId;

    public TearchClassParams(String userId) {
        this.userId = userId;
    }
}
