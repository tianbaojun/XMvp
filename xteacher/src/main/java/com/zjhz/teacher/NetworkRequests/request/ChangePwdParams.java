package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by xiangxue on 2016/6/7.
 */
public class ChangePwdParams implements Serializable{
    private String oldPwd;
    private String newPwd;
    private String newPwdAgain;
    private String userId;

    public ChangePwdParams(String oldPwd, String newPwd, String newPwdAgain, String userId) {
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
        this.newPwdAgain = newPwdAgain;
        this.userId = userId;
    }
}
