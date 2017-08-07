package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/13.
 */
public class OrderFlowStateBean implements Serializable{
    private String nodeName = "";
    private int status = -1;
    private int flowStatus = -1;
    private int approveFlag = -1;
    private int curNode = -1;

    public int getCurNode() {
        return curNode;
    }

    public void setCurNode(int curNode) {
        this.curNode = curNode;
    }

    public int getApproveFlag() {
        return approveFlag;
    }

    public void setApproveFlag(int approveFlag) {
        this.approveFlag = approveFlag;
    }

    public int getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(int flowStatus) {
        this.flowStatus = flowStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
