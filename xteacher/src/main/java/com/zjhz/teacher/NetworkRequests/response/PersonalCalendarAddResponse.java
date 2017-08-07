package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 个人行事历响应数据
 */
public class PersonalCalendarAddResponse implements Serializable {


    /**
     * msg : 新增成功!
     * code : 0
     * total : 1
     * data : {"calendarId":"265207280935374848"}
     */
    public String msg;
    public int code;
    public int total;

    /**
     * calendarId : 265207280935374848
     */
    public DataBean data;

    public static class DataBean implements Serializable{
        public String calendarId;
    }
}
