package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/15.
 */
public class PersonRepairsParams implements Serializable{
    private String name;
    private String startTime;
    private String endTime;

    public PersonRepairsParams(String name, String startTime, String endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
