package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tabjin on 2017/5/2.
 * Description:
 * What Changed:
 */

public class ClasszMementDocumentSectionDic implements Serializable {

    private String paramKey,dictId,parentId,paramValue;
    private List<ClasszMomentDocumentDicChild> sectionDicChildren;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public List<ClasszMomentDocumentDicChild> getSectionDicChildren() {
        return sectionDicChildren;
    }

    public void setSectionDicChildren(List<ClasszMomentDocumentDicChild> sectionDicChildren) {
        this.sectionDicChildren = sectionDicChildren;
    }
}
