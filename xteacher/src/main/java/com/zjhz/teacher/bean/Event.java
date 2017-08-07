package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description:
 */
public class Event implements Serializable {

    private String position;
    private String tagContent;

    public Event(String position, String tagContent) {
        this.position = position;
        this.tagContent = tagContent;
    }

    public String getPosition() {
        return position;
    }

    public String getTagContent() {
        return tagContent;
    }
}
