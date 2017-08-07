package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsMomentList implements Serializable {
    public ClasszMomentsMomentList(String classId, String page) {
        this.classId = classId;
        this.page = page;
    }

    public ClasszMomentsMomentList(String page, String classId, String dcId) {
        this.page = page;
        this.classId = classId;
        this.dcId = dcId;
    }

    private String classId,isPage = "1",page,pageSize = "5",dcId;

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setIsPage(String isPage) {
        this.isPage = isPage;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public String getClassId() {
        return classId;
    }

    public String getIsPage() {
        return isPage;
    }

    public String getPage() {
        return page;
    }

    public String getPageSize() {
        return pageSize;
    }
}
