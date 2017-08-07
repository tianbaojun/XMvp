package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 发布食谱
 * Created by Administrator on 2016/6/22.
 */
public class ReleaseFoodBean implements Serializable{
    private String content;
    private int pattern;
    private String imageUrls;

    public ReleaseFoodBean(String content, int pattern,String imageUrls) {
        this.content = content;
        this.pattern = pattern;
        this.imageUrls = imageUrls;
    }
}
