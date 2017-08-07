package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class PersonInfoBean implements Serializable{
    private String name = "";
    private String sexVal = "";
    private String phone = "";
    private String deptName = "";
    private String jobNumber = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSexVal() {
        return sexVal;
    }

    public void setSexVal(String sexVal) {
        this.sexVal = sexVal;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
