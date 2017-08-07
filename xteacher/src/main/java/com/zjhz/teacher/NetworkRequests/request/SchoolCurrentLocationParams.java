package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SchoolCurrentLocationParams implements Serializable{
    private String cardNum;

    public SchoolCurrentLocationParams(String cardNum) {
        this.cardNum = cardNum;
    }
}
