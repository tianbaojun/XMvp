package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 部门
 * Created by Administrator on 2016/7/19.
 */
public class SendDeptBean implements Serializable{
    private String name ="";
    private boolean isChecked= false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
