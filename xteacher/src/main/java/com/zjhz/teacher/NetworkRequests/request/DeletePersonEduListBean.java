package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/9.
 */
public class DeletePersonEduListBean implements Serializable{
    private String moralManIds;

    public DeletePersonEduListBean(String items) {
        this.moralManIds = items;
    }
}
