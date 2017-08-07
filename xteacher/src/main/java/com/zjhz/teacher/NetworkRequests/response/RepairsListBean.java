package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/13.
 */
public class RepairsListBean implements Serializable{
    private String repairId = "";
    private String applyer = "";
    private String summary = "";
    private String nickName = "";
    private String applyTime = "";
    private String createTime = "";
    private String orginAddress = "";
    private String itemName = "";
    private OrderFlowStateBean orderFlowState;
    private OrderFlowStateBean orderStateMsg;
    private int status;
    private String photoUrl;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrginAddress() {
        return orginAddress;
    }

    public void setOrginAddress(String orginAddress) {
        this.orginAddress = orginAddress;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public OrderFlowStateBean getOrderFlowState() {
        return orderFlowState;
    }

    public void setOrderFlowState(OrderFlowStateBean orderFlowState) {
        this.orderFlowState = orderFlowState;
    }

    public OrderFlowStateBean getOrderStateMsg() {
        return orderStateMsg;
    }

    public void setOrderStateMsg(OrderFlowStateBean orderStateMsg) {
        this.orderStateMsg = orderStateMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
