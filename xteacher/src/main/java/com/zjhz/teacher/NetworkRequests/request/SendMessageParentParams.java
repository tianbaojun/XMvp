package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/10.
 * Description:
 * What Changed:
 */

public class SendMessageParentParams implements Serializable {

    private String type;

    public SendMessageParentParams(String type) {
        this.type = type;
    }
}
