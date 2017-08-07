package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/5/2.
 * Description:
 * What Changed:
 */

public class ClasszMomentDocumentDicChild implements Serializable {
    private String paramKey,dictId,parentId,paramValue;

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
}
