package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 当前有效校历的配置信息响应数据
 */
public class SchoolCalendarManagerResponse implements Serializable {

    /**
     * code : 0
     * data : {"calendarId":"273940751065288704","createTime":"2016-07-22 13:57:39","createUser":0,"endTime":"2017-01-15 00:00:00","isActive":"1","schoolId":"271688204724211712","semester":523,"startTime":"2016-09-01 00:00:00","status":"1","title":"2016-2017学年第一学期","updateTime":"2016-07-23 10:51:05","updateUser":272484438246428672,"year":513}
     * msg : 查询到1条记录!
     * total : 1
     */

    public int code;
    /**
     * calendarId : 273940751065288704
     * createTime : 2016-07-22 13:57:39
     * createUser : 0
     * endTime : 2017-01-15 00:00:00
     * isActive : 1
     * schoolId : 271688204724211712
     * semester : 523
     * startTime : 2016-09-01 00:00:00
     * status : 1
     * title : 2016-2017学年第一学期
     * updateTime : 2016-07-23 10:51:05
     * updateUser : 272484438246428672
     * year : 513
     */

    public DataBean data;
    public String msg;
    public int total;

    public static class DataBean implements Serializable {
        public String calendarId;
        public String createTime;
        public int createUser;
        public String endTime;
        public String isActive;
        public String schoolId;
        public int semester;
        public String startTime;
        public String status;
        public String title;
        public String updateTime;
        public long updateUser;
        public int year;
    }
}
