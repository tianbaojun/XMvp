package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DeptAndTeacherBean implements Serializable{
    private String deptName;
    private List<TeacherBean> users;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<TeacherBean> getUsers() {
        return users;
    }

    public void setUsers(List<TeacherBean> users) {
        this.users = users;
    }
}
