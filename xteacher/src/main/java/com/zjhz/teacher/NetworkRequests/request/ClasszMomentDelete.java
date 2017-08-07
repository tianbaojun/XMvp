package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/5/5.
 * Description:
 * What Changed:
 */

public class ClasszMomentDelete implements Serializable {
    private String dcId;

    public ClasszMomentDelete(String dcId) {
        this.dcId = dcId;
    }
}
