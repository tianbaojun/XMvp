package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-08
 * Time: 16:57
 * Description: 校外定位
 */
public class StudentCurrentPositionResponse implements Serializable{

    /**
     * code : 0
     * data : [{"deviceType":"0","pointxBaidu":"120.609565","pointyBaidu":"30.077977","recordTime":"2016-09-01 16:30:03","stationAddress":"智鹏上市有限公司","stationCode":"88888888"}]
     * msg : 查询到1条记录!
     * total : 1
     */

    public int code;
    public String msg;
    public int total;
    /**
     * deviceType : 0
     * pointxBaidu : 120.609565
     * pointyBaidu : 30.077977
     * recordTime : 2016-09-01 16:30:03
     * stationAddress :
     * stationCode : 88888888
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String deviceType;
        public String pointxBaidu;
        public String pointyBaidu;
        public String recordTime;
        public String stationAddress;
        public String stationCode;
    }
}
