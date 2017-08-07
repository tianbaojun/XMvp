package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * 发布作业
 * Created by Administrator on 2016/6/25.
 */
public class EditWorkParams implements Serializable{
    private String homeworkName;
    private String subjectId;
    private String costTime;
    private String deliverTime;
    private String content;
    private String classIds;
    private String images;
    private String homeworkId;
    private String homeworkType;
    private String homeworkNature;
    private String shareFlag;

    public EditWorkParams() {
    }

    public EditWorkParams(String subjectId, String costTime, String deliverTime, String content, String classIds, String images) {
        this.subjectId = subjectId;
        this.costTime = costTime;
        this.deliverTime = deliverTime;
        this.content = content;
        this.classIds = classIds;
        this.images = images;
    }
    //第一步数据
    public EditWorkParams(String subjectId, String costTime, String deliverTime, String classIds) {
        this.subjectId = subjectId;
        this.costTime = costTime;
        this.deliverTime = deliverTime;
        this.classIds = classIds;
    }

    public EditWorkParams(String subjectId, String costTime, String deliverTime, String content, String classIds, String images, String homeworkId) {
        this.subjectId = subjectId;
        this.costTime = costTime;
        this.deliverTime = deliverTime;
        this.content = content;
        this.classIds = classIds;
        this.images = images;
        this.homeworkId = homeworkId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClassIds() {
        return classIds;
    }

    public void setClassIds(String classIds) {
        this.classIds = classIds;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public String getHomeworkType() {
        return homeworkType;
    }

    public void setHomeworkType(String homeworkType) {
        this.homeworkType = homeworkType;
    }

    public String getHomeworkNature() {
        return homeworkNature;
    }

    public void setHomeworkNature(String homeworkNature) {
        this.homeworkNature = homeworkNature;
    }

    public String getShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(String shareFlag) {
        this.shareFlag = shareFlag;
    }
}
