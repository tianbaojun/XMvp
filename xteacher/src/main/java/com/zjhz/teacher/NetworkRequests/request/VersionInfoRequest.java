package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by zzd on 2017/4/21.
 */

public class VersionInfoRequest implements Serializable {
    public String osType;  //传1表示安卓
    public String terminalCode;
    public String appVersion;

    public VersionInfoRequest(String osType, String terminalCode, String appVersion) {
        this.osType = osType;
        this.terminalCode = terminalCode;
        this.appVersion = appVersion;
    }
}
