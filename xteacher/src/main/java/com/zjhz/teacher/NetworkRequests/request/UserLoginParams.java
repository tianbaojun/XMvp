package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

public class UserLoginParams implements Serializable{
    private String userName;
    private String pwd;
    private String os;
    private String inSchoolId;
    private String appType = "teacher";  //teacher
    private String imei;

    public UserLoginParams(String userName, String pwd, String os, String schoolId) {
        this.userName = userName;
        this.pwd = pwd;
        this.os = os;
        this.inSchoolId = schoolId;
    }

    public UserLoginParams(String userName, String pwd,String os) {
        this.userName = userName;
        this.pwd = pwd;
        this.os = os;
    }

    public UserLoginParams(String userName, String pwd, String os, String appType, String imei) {
        this.userName = userName;
        this.pwd = pwd;
        this.os = os;
        this.appType = appType;
        this.imei = imei;
    }
}
