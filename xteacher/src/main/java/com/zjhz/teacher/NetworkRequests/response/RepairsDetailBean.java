package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class RepairsDetailBean implements Serializable{
    private String applyTime = "";
    private String itemName= "";
    private String orginAddress= "";
    private String dutyDept= "";
    private String summary= "";
    private String createUser= "";
    private String nickName= "";
    private String dutyDeptVal = "";
    private String photoUrl = "";
    private int dtlTotal = 0;
    private List<ImageBean> imgs = null;
    private List<RepairsTaskListBean> taskList= null;
    private int status;

    public int getDtlTotal() {
        return dtlTotal;
    }

    public void setDtlTotal(int dtlTotal) {
        this.dtlTotal = dtlTotal;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public String getDutyDeptVal() {
        return dutyDeptVal;
    }

    public void setDutyDeptVal(String dutyDeptVal) {
        this.dutyDeptVal = dutyDeptVal;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOrginAddress() {
        return orginAddress;
    }

    public void setOrginAddress(String orginAddress) {
        this.orginAddress = orginAddress;
    }

    public String getDutyDept() {
        return dutyDept;
    }

    public void setDutyDept(String dutyDept) {
        this.dutyDept = dutyDept;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<ImageBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImageBean> imgs) {
        this.imgs = imgs;
    }

    public List<RepairsTaskListBean> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<RepairsTaskListBean> taskList) {
        this.taskList = taskList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
