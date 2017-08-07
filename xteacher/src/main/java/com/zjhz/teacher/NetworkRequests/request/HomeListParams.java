package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/28.
 */
public class HomeListParams implements Serializable {
    public String inSchoolId;
    public String pageSize;
    public String page;
    public String terminalCode;

    public HomeListParams(String page,String inSchoolId,String pageSize,String terminalCode) {
        this.page = page;
        this.inSchoolId = inSchoolId;
        this.pageSize = pageSize;
        this.terminalCode = terminalCode;
    }
}
