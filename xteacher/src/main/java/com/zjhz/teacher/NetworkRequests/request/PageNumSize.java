package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/24.
 */
public class PageNumSize implements Serializable{
    private int page;
    private int pageSize;

    public PageNumSize(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

}
