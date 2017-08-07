package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/12.
 */
public class PushListParams implements Serializable{
    private String inSchoolId;
    private String pageSize;

    public PushListParams(String inSchoolId, String pageSize) {
        this.inSchoolId = inSchoolId;
        this.pageSize = pageSize;
    }
}
