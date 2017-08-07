package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Term implements Serializable{

    private String startTime,endTime,title;

    public Term(){}

    public Term(String startTime, String endTime, String title) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTitle() {
        return title;
    }
}
