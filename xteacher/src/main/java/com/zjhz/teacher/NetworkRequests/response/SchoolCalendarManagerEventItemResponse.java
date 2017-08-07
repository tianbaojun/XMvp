package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 校历的列表数据
 * 修改 2016。7.23
 */
public class SchoolCalendarManagerEventItemResponse implements Serializable {


    /**
     * code : 0
     * data : [{"calendarId":"273940751065288704","content":"集团小语文、教学研讨活动","createTime":"2016-07-23 10:44:52","createUser":272484438246428672,"scheduleId":"274254622221144064","schoolId":"271688204724211712","status":"1","weekNum":5},{"calendarId":"273940751065288704","content":"个教研组、学科组活动","createTime":"2016-07-23 10:45:05","createUser":272484438246428672,"scheduleId":"274254677250412544","schoolId":"271688204724211712","status":"1","weekNum":5}]
     * msg : 查询结果
     * total : 2
     */

    public int code;
    public String msg;
    public int total;
    /**
     * calendarId : 273940751065288704
     * content : 集团小语文、教学研讨活动
     * createTime : 2016-07-23 10:44:52
     * createUser : 272484438246428672
     * scheduleId : 274254622221144064
     * schoolId : 271688204724211712
     * status : 1
     * weekNum : 5
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String calendarId;
        public String content;
        public String createTime;
        public long createUser;
        public String scheduleId;
        public String schoolId;
        public String status;
        public int weekNum;
    }
}
