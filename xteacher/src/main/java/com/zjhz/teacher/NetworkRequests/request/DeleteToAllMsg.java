package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/8.
 */

public class DeleteToAllMsg implements Serializable {
    String msgId;

    public DeleteToAllMsg(String msgId) {
        this.msgId = msgId;
    }
}
