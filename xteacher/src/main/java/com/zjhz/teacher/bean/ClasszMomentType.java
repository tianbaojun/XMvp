package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentType implements Serializable {
    private String dcId,createTime,dcName,schoolId,dcCode,describes;

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setDcCode(String dcCode) {
        this.dcCode = dcCode;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getDcId() {
        return dcId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDcName() {
        return dcName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getDcCode() {
        return dcCode;
    }

    public String getDescribes() {
        return describes;
    }

}
