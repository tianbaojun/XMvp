package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-06
 * Time: 16:57
 * Description: 校外定位  上学放学历史路经查询
 */
public class SchoolPathRequest implements Serializable{

    public String cardNum;
    public String date;
    public String flag; // 标记0上学路径，1放学路径

    public SchoolPathRequest(String cardNum, String date, String flag) {
        this.cardNum = cardNum;
        this.date = date;
        this.flag = flag;
    }
}
