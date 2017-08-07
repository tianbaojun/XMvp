package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/8.
 */
public class WeekOfTerm implements Serializable {

    private String  endTime,index,startTime,week;

    public String getEndTime() {
        return endTime;
    }

    public String getIndex() {
        return index;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getWeek() {
        return week;
    }
}
