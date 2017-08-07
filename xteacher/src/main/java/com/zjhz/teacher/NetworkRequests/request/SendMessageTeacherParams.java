package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/10.
 * Description:
 * What Changed:
 */

public class SendMessageTeacherParams implements Serializable {
    private String type;

    public SendMessageTeacherParams(String type) {
        this.type = type;
    }
}
