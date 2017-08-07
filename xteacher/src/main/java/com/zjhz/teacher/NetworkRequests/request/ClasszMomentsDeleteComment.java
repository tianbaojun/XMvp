package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsDeleteComment implements Serializable {

    public ClasszMomentsDeleteComment(String dcId) {
        this.dcId = dcId;
    }

    private String dcId;

    public String getDcId() {
        return dcId;
    }
}
