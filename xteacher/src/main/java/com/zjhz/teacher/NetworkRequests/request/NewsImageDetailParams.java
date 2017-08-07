package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/11.
 */
public class NewsImageDetailParams implements Serializable{
    private String imageId;

    public NewsImageDetailParams(String imageId) {
        this.imageId = imageId;
    }
}
