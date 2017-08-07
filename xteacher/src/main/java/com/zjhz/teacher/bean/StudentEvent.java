package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 校外定位Event
 */
public class StudentEvent implements Serializable {

    private String cardNum;
    private String date;
    private String flag;

    public StudentEvent(String cardNum, String date, String flag) {
        this.cardNum = cardNum;
        this.date = date;
        this.flag = flag;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getDate() {
        return date;
    }

    public String getFlag() {
        return flag;
    }
}
