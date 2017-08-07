package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 个人行事历 相应数据
 */
public class PersonalCalendarResponse implements Serializable {

    /**
     * code : 0
     * data : {"calendar":[{"calendarId":"274349468420149248","createTime":"2016-07-23 17:01:45","createUser":272484438351286272,"priority":0,"scheduleTime":"2016-07-28 00:00:00","scheduleType":0,"schoolId":"271688204724211712","teacherId":"272484438347091968","title":"了来咯的啊"}],"dutyDay":[]}
     * msg : 查询到1条记录!
     * total : 1
     */

    public int code;
    public DataBean data;
    public String msg;
    public int total;

    public static class DataBean implements Serializable{
        /**
         * calendarId : 274349468420149248
         * createTime : 2016-07-23 17:01:45
         * createUser : 272484438351286272
         * priority : 0
         * scheduleTime : 2016-07-28 00:00:00
         * scheduleType : 0
         * schoolId : 271688204724211712
         * teacherId : 272484438347091968
         * title : 了来咯的啊
         */

        public List<CalendarBean> calendar;
        public List<?> dutyDay;

        public static class CalendarBean implements Serializable{
            public String calendarId;
            public String createTime;
            public long createUser;
            public int priority;
            public String scheduleTime;
            public int scheduleType;
            public String schoolId;
            public String teacherId;
            public String title;
        }
    }
}
