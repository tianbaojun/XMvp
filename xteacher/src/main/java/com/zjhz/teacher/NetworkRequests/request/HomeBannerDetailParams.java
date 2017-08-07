package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**首页图片获取详情
 * Created by Administrator on 2016/7/28.
 */
public class HomeBannerDetailParams implements Serializable{
    public String imageId;

    public HomeBannerDetailParams(String imageId) {
        this.imageId = imageId;
    }
}
