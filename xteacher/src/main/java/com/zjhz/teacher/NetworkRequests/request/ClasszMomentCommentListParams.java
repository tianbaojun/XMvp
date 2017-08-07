package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/27.
 * Description:
 * What Changed:
 */

public class ClasszMomentCommentListParams implements Serializable {
    private String dId;

    public ClasszMomentCommentListParams(String dId) {
        this.dId = dId;
    }
}
