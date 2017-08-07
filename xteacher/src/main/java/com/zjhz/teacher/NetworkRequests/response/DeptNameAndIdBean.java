package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * 申请报修部门选择保存在本地
 * Created by Administrator on 2016/7/19.
 */
public class DeptNameAndIdBean implements Serializable {
    private List<String> deptIds;
    private List<String> deptNames;

    public List<String> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<String> deptIds) {
        this.deptIds = deptIds;
    }

    public List<String> getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(List<String> deptNames) {
        this.deptNames = deptNames;
    }
}
