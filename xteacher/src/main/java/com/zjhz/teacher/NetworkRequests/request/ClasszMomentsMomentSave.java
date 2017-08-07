package com.zjhz.teacher.NetworkRequests.request;

import com.zjhz.teacher.bean.AudioBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsMomentSave implements Serializable {
    public ClasszMomentsMomentSave( String ccdTitle, String classId, String code, String dcId, String isshare, String picUrls) {
        this.ccdContent = ccdTitle;
        this.classId = classId;
        this.sectionCode = code;
        this.dcId = dcId;
        //是否属于分享的动态,0否，1是
        this.isshare = isshare;
        this.picUrls = picUrls;
    }

    private String ccdContent,classId,sectionCode,dcId,isshare,picUrls, type, parentId;

    private String attPicPath; //缩略图
    private String attPath; //附件

    private List<AudioBean> voiceList;

    public ClasszMomentsMomentSave() {
    }

    public List<AudioBean> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<AudioBean> voiceList) {
        this.voiceList = voiceList;
    }

    public String getCcdContent() {
        return ccdContent;
    }

    public void setCcdContent(String ccdContent) {
        this.ccdContent = ccdContent;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }


    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public void setIsshare(String isshare) {
        this.isshare = isshare;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls = picUrls;
    }

    public String getClassId() {
        return classId;
    }


    public String getDcId() {
        return dcId;
    }

    public String getIsshare() {
        return isshare;
    }

    public String getPicUrls() {
        return picUrls;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAttPicPath() {
        return attPicPath;
    }

    public void setAttPicPath(String attPicPath) {
        this.attPicPath = attPicPath;
    }

    public String getAttPath() {
        return attPath;
    }

    public void setAttPath(String attPath) {
        this.attPath = attPath;
    }
}
