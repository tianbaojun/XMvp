package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 获取所有个人行事历信息  相应数据
 */
public class PersonalCalendarAllResponse implements Serializable {


    /**
     * code : 0
     * data : {"calendar":[{"scheduleTime":"2016-07-14 00:00:00"}],"dutyDay":[{"ddayId":"265597820927479808","dutyTime":"2016-07-06 00:00:00","teachers":[{"dutyTime":"2016-07-06 00:00:00"},{"dutyTime":"2016-07-06 00:00:00"},{"dutyTime":"2016-07-06 00:00:00"}]},{"ddayId":"268145833478721536","dutyTime":"2016-07-06 00:00:00","teachers":[{"dutyTime":"2016-07-06 00:00:00"},{"dutyTime":"2016-07-06 00:00:00"},{"dutyTime":"2016-07-06 00:00:00"}]},{"ddayId":"265598009796988928","dutyTime":"2016-07-03 00:00:00","teachers":[{"dutyTime":"2016-07-03 00:00:00"},{"dutyTime":"2016-07-03 00:00:00"},{"dutyTime":"2016-07-03 00:00:00"},{"dutyTime":"2016-07-03 00:00:00"}]},{"ddayId":"269151039574249472","dutyTime":"2016-07-02 00:00:00","teachers":[{"dutyTime":"2016-07-02 00:00:00"},{"dutyTime":"2016-07-02 00:00:00"},{"dutyTime":"2016-07-02 00:00:00"}]}]}
     * msg : 查询到-1条记录!
     * total : -1
     */

    public int code;
    public DataBean data;
    public String msg;
    public int total;

    public static class DataBean  implements Serializable {
        /**
         * scheduleTime : 2016-07-14 00:00:00
         */

        public List<CalendarBean> calendar;

        public static class CalendarBean  implements Serializable {
            public String scheduleTime;   // 当前时间
        }

        /**
         * ddayId : 265597820927479808
         * dutyTime : 2016-07-06 00:00:00
         * teachers : [{"dutyTime":"2016-07-06 00:00:00"},{"dutyTime":"2016-07-06 00:00:00"},{"dutyTime":"2016-07-06 00:00:00"}]
         */

        public List<DutyDayBean> dutyDay;

        public static class DutyDayBean  implements Serializable {
            public String ddayId;
            public String dutyTime;  // 值日时间
            /**
             * dutyTime : 2016-07-06 00:00:00
             */

            public List<TeachersBean> teachers;

            public static class TeachersBean  implements Serializable {
                public String dutyTime;
            }
        }
    }
}
