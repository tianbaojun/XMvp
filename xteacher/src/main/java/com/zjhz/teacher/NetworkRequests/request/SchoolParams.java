package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/22.
 */
public class SchoolParams implements Serializable{
    private String id = "";

    public SchoolParams(String id) {
        this.id = id;
    }
}
