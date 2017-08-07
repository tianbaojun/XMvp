package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/14.
 */
public class RepairSubParams implements Serializable{
    private String oid;

    public RepairSubParams(String repairId) {
        this.oid = repairId;
    }

    public String getRepairId() {
        return oid;
    }

    public void setRepairId(String repairId) {
        this.oid = repairId;
    }
}
