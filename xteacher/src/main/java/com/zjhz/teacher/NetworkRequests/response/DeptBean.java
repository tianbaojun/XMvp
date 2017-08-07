package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 部门列表
 * Created by Administrator on 2016/7/13.
 */
public class DeptBean implements Serializable{
    private String deptName;
    private String deptId;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
