package com.zjhz.teacher.NetworkRequests.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 请求响应数据
 */
public class User implements Serializable{

    @SerializedName("code")
    public String code;

    @SerializedName("content")
    public String content;
}
