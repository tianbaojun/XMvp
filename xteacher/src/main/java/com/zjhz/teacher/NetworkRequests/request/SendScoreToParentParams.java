package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/9.
 */
public class SendScoreToParentParams implements Serializable{
    private String stuscoreClassId;
    private String msg;
    private String type;
    private String scoreMethod;

    public SendScoreToParentParams(String stuscoreClassId, String msg, String type, String scoreMethod) {
        this.stuscoreClassId = stuscoreClassId;
        this.msg = msg;
        this.type = type;
        this.scoreMethod = scoreMethod;
    }

}
