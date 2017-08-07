package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 取消点赞获取的id
 * Created by Administrator on 2016/6/20.
 */
public class PraiseIdBean implements Serializable{
    private String praiseId;

    public String getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(String praiseId) {
        this.praiseId = praiseId;
    }
}
