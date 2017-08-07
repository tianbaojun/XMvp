package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsHonourSave implements Serializable {

    private String dcId,sectionCode,classId,ccdTitle,ccdContent,picUrls;

    public ClasszMomentsHonourSave(String dcId, String sectionCode, String classId, String ccdTitle, String ccdContent, String picUrls) {
        this.dcId = dcId;
        this.sectionCode = sectionCode;
        this.classId = classId;
        this.ccdTitle = ccdTitle;
        this.ccdContent = ccdContent;
        this.picUrls = picUrls;
    }

    public ClasszMomentsHonourSave() {
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCcdTitle() {
        return ccdTitle;
    }

    public void setCcdTitle(String ccdTitle) {
        this.ccdTitle = ccdTitle;
    }

    public String getCcdContent() {
        return ccdContent;
    }

    public void setCcdContent(String ccdContent) {
        this.ccdContent = ccdContent;
    }

    public String getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls = picUrls;
    }
}
