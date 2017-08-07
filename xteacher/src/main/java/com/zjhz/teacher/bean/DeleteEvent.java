package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DeleteEvent implements Serializable {
    private String position;
    private String day;

    public String getPosition() {
        return position;
    }

    public String getDay() {
        return day;
    }

    public DeleteEvent(String position, String day) {
        this.position = position;
        this.day = day;
    }
}
