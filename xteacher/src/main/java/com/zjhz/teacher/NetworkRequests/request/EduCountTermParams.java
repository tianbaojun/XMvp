package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class EduCountTermParams implements Serializable {
    private String calType;
    public EduCountTermParams(String calType){
        this.calType = calType;
    }
}
