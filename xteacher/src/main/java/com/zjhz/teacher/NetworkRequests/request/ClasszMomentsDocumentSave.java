package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsDocumentSave implements Serializable {
    public ClasszMomentsDocumentSave(String appkey, String attName, String attPath, String ccdContent, String classId, String code, String dcId, String isshare, String keywords) {
        this.appkey = appkey;
        this.attName = attName;
        this.attPath = attPath;
        this.ccdContent = ccdContent;
        this.classId = classId;
        this.code = code;
        this.dcId = dcId;
        this.isshare = isshare;
        this.keywords = keywords;
    }

    public ClasszMomentsDocumentSave() {
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public void setAttPath(String attPath) {
        this.attPath = attPath;
    }

    public String getCcdContent() {
        return ccdContent;
    }

    public void setCcdContent(String ccdContent) {
        this.ccdContent = ccdContent;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public void setIsshare(String isshare) {
        this.isshare = isshare;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    private String appkey,attName,attPath,ccdContent,classId,code,dcId,isshare,keywords,type,sectionCode;

    public String getAppkey() {
        return appkey;
    }

    public String getAttName() {
        return attName;
    }

    public String getAttPath() {
        return attPath;
    }

    public String getClassId() {
        return classId;
    }

    public String getCode() {
        return code;
    }

    public String getDcId() {
        return dcId;
    }

    public String getIsshare() {
        return isshare;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public String toString() {
        return "ClasszMomentsDocumentSave{" +
                "attName='" + attName + '\'' +
                ", attPath='" + attPath + '\'' +
                ", ccdContent='" + ccdContent + '\'' +
                ", classId='" + classId + '\'' +
                ", dcId='" + dcId + '\'' +
                ", keywords='" + keywords + '\'' +
                ", type='" + type + '\'' +
                ", sectionCode='" + sectionCode + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
