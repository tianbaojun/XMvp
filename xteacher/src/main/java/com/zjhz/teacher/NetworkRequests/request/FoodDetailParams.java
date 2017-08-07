package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/22.
 */
public class FoodDetailParams implements Serializable{
    private String cookbookId;

    public FoodDetailParams(String cookbookId) {
        this.cookbookId = cookbookId;
    }
}
