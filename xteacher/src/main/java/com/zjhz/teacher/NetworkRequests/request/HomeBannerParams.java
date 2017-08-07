package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/28.
 */
public class HomeBannerParams implements Serializable{
    public String page;
    public String pageSize;

    public HomeBannerParams(String page, String pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
}
