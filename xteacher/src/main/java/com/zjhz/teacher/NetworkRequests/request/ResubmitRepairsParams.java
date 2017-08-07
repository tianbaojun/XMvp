package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ResubmitRepairsParams implements Serializable{
    private String repairId;
    private String applyTime;
    private String itemName;
    private String orginAddress;
    private String dutyDept;
    private String summary;
    private String imageUrls;

    /**
     * 修改报修单提交
     * @param applyTime
     * @param itemName
     * @param orginAddress
     * @param dutyDept
     * @param summary
     * @param imageUrls
     * @param repairId
     */
    public ResubmitRepairsParams(String applyTime, String itemName, String orginAddress, String dutyDept, String summary, String imageUrls, String repairId) {
        this.applyTime = applyTime;
        this.itemName = itemName;
        this.orginAddress = orginAddress;
        this.dutyDept = dutyDept;
        this.summary = summary;
        this.imageUrls = imageUrls;
        this.repairId = repairId;
    }
}
