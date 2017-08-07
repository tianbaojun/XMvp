package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/14.
 */
public class RepairsTaskListBean implements Serializable{
    private String nodeName = "";
    private String taskUser= "";
    private String content= "";
    private String flowTime= "";
    private int status = -1;
    private boolean isApproveFlag = false;
    private String photoUrl = "";
    private String nickName = "";
    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getStatus() {
        return status;
    }

    public boolean isApproveFlag() {
        return isApproveFlag;
    }

    public void setApproveFlag(boolean approveFlag) {
        isApproveFlag = approveFlag;
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

    public String getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(String taskUser) {
        this.taskUser = taskUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlowTime() {
        return flowTime;
    }

    public void setFlowTime(String flowTime) {
        this.flowTime = flowTime;
    }
}
