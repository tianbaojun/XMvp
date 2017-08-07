package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/25.
 * Description:
 * What Changed:
 */

public class ClasszMomentPraiseListParam implements Serializable {
    private String dId;

    public ClasszMomentPraiseListParam(String dId) {
        this.dId = dId;
    }
}
