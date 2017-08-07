package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/27.
 */
public class OutgoingParams implements Serializable {
    private String  annContent;

    public OutgoingParams(String annContent) {
        this.annContent = annContent;
    }
}
