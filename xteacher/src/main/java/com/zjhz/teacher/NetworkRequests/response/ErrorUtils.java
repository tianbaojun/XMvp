package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 请求响应错误数据
 */
public class ErrorUtils implements Serializable {

    String message;

    public ErrorUtils(String message) {
        this.message = message;
    }
}
