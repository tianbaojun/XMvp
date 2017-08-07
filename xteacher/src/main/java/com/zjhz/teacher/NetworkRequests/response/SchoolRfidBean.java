package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class SchoolRfidBean implements Serializable{
    private String rfidCard = "";

    public String getRfidCard() {
        return rfidCard;
    }

    public void setRfidCard(String rfidCard) {
        this.rfidCard = rfidCard;
    }
}
