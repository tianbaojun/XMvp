package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/9.
 */
public class CheckRequest implements Serializable{

    public String osType;  //传1表示安卓
    public String terminalCode;

    public CheckRequest(String osType, String terminalCode) {
        this.osType = osType;
        this.terminalCode = terminalCode;
    }
}
