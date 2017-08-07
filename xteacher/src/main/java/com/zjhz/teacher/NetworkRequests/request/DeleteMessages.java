package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/10.
 */

public class DeleteMessages implements Serializable {
    private String linkIds,phoneNo;


    public DeleteMessages(String linkIds,String phoneNo) {
        this.linkIds = linkIds;
        this.phoneNo = phoneNo;
    }

}
