package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 角色对象
 * Created by Administrator on 2016/6/24.
 */
public class RolesBean implements Serializable{
    private String roleId;
    private String roleName;
    private String roleType;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}
