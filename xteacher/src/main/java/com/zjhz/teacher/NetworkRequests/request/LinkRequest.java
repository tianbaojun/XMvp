package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/22.
 */
public class LinkRequest implements Serializable {

    public String teacherId;

    public LinkRequest(String teacherId) {
        this.teacherId = teacherId;
    }
}
