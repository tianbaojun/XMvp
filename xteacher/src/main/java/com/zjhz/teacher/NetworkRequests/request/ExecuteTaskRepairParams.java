package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ExecuteTaskRepairParams implements Serializable{
    private String repairId;
    private String desc;

    public ExecuteTaskRepairParams(String repairId, String desc) {
        this.repairId = repairId;
        this.desc = desc;
    }
}
