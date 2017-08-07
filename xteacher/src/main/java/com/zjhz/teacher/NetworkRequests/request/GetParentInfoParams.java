package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 根据班级id获取家长信息
 * Created by Administrator on 2016/7/20.
 */
public class GetParentInfoParams implements Serializable{
    private String classIds;

    public GetParentInfoParams(String gradeId) {
        this.classIds = gradeId;
    }
}
