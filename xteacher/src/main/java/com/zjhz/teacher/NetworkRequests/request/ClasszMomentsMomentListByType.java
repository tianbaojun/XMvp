package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/4/23.
 * Description:
 * What Changed:
 */

public class ClasszMomentsMomentListByType implements Serializable {
    public ClasszMomentsMomentListByType(String classId, String isPage, String page, String pageSize, String dcId) {
        this.classId = classId;
        this.isPage = isPage;
        this.page = page;
        this.pageSize = pageSize;
        this.dcId = dcId;
    }

    private String classId,isPage,page,pageSize,dcId;

    public String getDcId() {
        return dcId;
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
