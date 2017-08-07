package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请 详情 同意
 */
public class AgreeRequest implements Serializable{

    public String oid;
    public String desc;

    public AgreeRequest(String oid, String desc) {
        this.oid = oid;
        this.desc = desc;
    }
}
