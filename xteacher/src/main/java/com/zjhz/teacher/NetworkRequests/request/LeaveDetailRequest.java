package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/17.
 */
public class LeaveDetailRequest implements Serializable {
    public String linkId;

    public LeaveDetailRequest(String linkId) {
        this.linkId = linkId;
    }
}
