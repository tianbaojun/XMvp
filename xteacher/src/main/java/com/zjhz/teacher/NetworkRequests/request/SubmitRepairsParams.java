package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/14.
 */
public class SubmitRepairsParams implements Serializable{
    private String applyTime;
    private String itemName;
    private String orginAddress;
    private String dutyDept;
    private String summary;
    private String imageUrls;

    /**
     *申请报修提交参数
     * @param applyTime
     * @param itemName
     * @param orginAddress
     * @param dutyDept
     * @param summary
     * @param imageUrls
     */
    public SubmitRepairsParams(String applyTime, String itemName, String orginAddress, String dutyDept, String summary, String imageUrls) {
        this.applyTime = applyTime;
        this.itemName = itemName;
        this.orginAddress = orginAddress;
        this.dutyDept = dutyDept;
        this.summary = summary;
        this.imageUrls = imageUrls;
    }
}
