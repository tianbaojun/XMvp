package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsPraiseOrCancel implements Serializable {

    public ClasszMomentsPraiseOrCancel(String dId, String dpStatus, String roleType) {
        this.dId = dId;
        this.dpStatus = dpStatus;
        this.roleType = roleType;
    }

    private String dId,dpStatus,roleType;

    public String getdId() {
        return dId;
    }

    public String getDpStatus() {
        return dpStatus;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public void setDpStatus(String dpStatus) {
        this.dpStatus = dpStatus;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}
