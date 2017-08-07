package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/24.
 */
public class DeleteSendMeetParams implements Serializable {
    private String meetId;

    public DeleteSendMeetParams(String meetId) {
        this.meetId = meetId;
    }
}
