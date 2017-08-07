package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请 详情 不同意
 */
public class UnAgreeRequest implements Serializable{

    public String oid;
    public String desc;

    public UnAgreeRequest(String oid, String desc) {
        this.oid = oid;
        this.desc = desc;
    }
}
