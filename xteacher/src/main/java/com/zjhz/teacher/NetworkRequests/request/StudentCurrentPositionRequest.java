package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 校外定位 获取学生当前位置
 */
public class StudentCurrentPositionRequest implements Serializable {
    public String cardNum;

    public StudentCurrentPositionRequest(String cardNum) {
        this.cardNum = cardNum;
    }
}
