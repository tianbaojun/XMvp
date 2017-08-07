package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class SchoolAllLocationBean implements Serializable{
    private List<SchoolLocationBean> been;

    public List<SchoolLocationBean> getBeen() {
        return been;
    }

    public void setBeen(List<SchoolLocationBean> been) {
        this.been = been;
    }
}
