package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/2.
 */
public class HeadImage implements Serializable {

    private String photoUrl;
    private String userId;


    public HeadImage(String photoUrl,String userId) {
        this.photoUrl = photoUrl;
        this.userId = userId;
    }
}
