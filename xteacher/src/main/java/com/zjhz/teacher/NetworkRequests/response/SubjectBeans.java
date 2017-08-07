package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 科目
 * Created by Administrator on 2016/6/27.
 */
public class SubjectBeans implements Serializable{
    private String subjectId;
    private String name;

    private boolean isCheck;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
