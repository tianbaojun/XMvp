package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请内容 请求参数
 */
public class LeaveApplyForContentTypeRequest implements Serializable {

    public String paramKeys;  //"OA_HOLIDAY_TYPE"

    public LeaveApplyForContentTypeRequest(String paramKeys) {
        this.paramKeys = paramKeys;
    }
}
