package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SchoolSideRouteParams implements Serializable{
    private String cardNum;
    private String date;
    private String flag;

    public SchoolSideRouteParams(String cardNum, String date, String flag) {
        this.cardNum = cardNum;
        this.date = date;
        this.flag = flag;
    }
}
