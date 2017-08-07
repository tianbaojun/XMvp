package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表
 */
public class LeaveApplyForListHistoryResponse implements Serializable {

    /**
     * code : 0
     * msg :
     * data : [{"applyer":210001,"applyerTime":"2016-04-08 00:00:00","clazz":2,"createTime":"2016-07-04 00:00:00","createUser":0,"ema":1,"endTime":"2016-04-08 12:40:44","oid":70886837,"orderStateMsg":{"buisCode":"70886837","createTime":"2016-07-06 13:52:10","createUser":210001,"curNode":3,"flowCode":"AL-2016-001","flowStatus":"1","instId":"268141165688131584","nodeName":"校长室审批03","schoolId":"1","status":"1","taskGroup":1,"taskType":0,"taskUser":260599263199760384,"updateTime":"2016-07-06 15:23:36","updateUser":260599263199760384},"schoolId":"1","sma":0,"startTime":"2016-04-08 12:40:44","subjectId":12,"summary":"fdafdafdafdaf","teacherId":1,"type":1,"week":1},{"applyer":222,"applyerTime":"2016-07-04 00:00:00","clazz":2,"createTime":"2016-07-04 00:00:00","createUser":0,"ema":2,"endTime":"2016-07-04 12:40:44","oid":99133886,"orderStateMsg":{"buisCode":"99133886","createTime":"2016-07-05 17:23:54","createUser":210001,"curNode":3,"flowCode":"AL-2016-001","flowStatus":"1","instId":"267832060297613312","nodeName":"校长室审批03","schoolId":"1","status":"1","taskGroup":1,"taskType":0,"taskUser":260599263199760384,"updateTime":"2016-07-06 11:30:01","updateUser":260599263199760384},"schoolId":"1","sma":2,"startTime":"2016-07-04 12:40:44","subjectId":2,"summary":"ffffffff","teacherId":2,"type":2,"updateTime":"2016-07-06 19:08:00","updateUser":-1,"week":2}]
     */

    public int code;
    public String msg;
    /**
     * applyer : 210001
     * applyerTime : 2016-04-08 00:00:00
     * clazz : 2
     * createTime : 2016-07-04 00:00:00
     * createUser : 0
     * ema : 1
     * endTime : 2016-04-08 12:40:44
     * oid : 70886837
     * orderStateMsg : {"buisCode":"70886837","createTime":"2016-07-06 13:52:10","createUser":210001,"curNode":3,"flowCode":"AL-2016-001","flowStatus":"1","instId":"268141165688131584","nodeName":"校长室审批03","schoolId":"1","status":"1","taskGroup":1,"taskType":0,"taskUser":260599263199760384,"updateTime":"2016-07-06 15:23:36","updateUser":260599263199760384}
     * schoolId : 1
     * sma : 0
     * startTime : 2016-04-08 12:40:44
     * subjectId : 12
     * summary : fdafdafdafdaf
     * teacherId : 1
     * type : 1
     * week : 1
     */

    public List<DataBean> data;

    public static class DataBean  implements Serializable {
        public int applyer;
        public String applyerTime;
        public int clazz;
        public String createTime;
        public int createUser;
        public int ema;
        public String endTime;
        public int oid;
        /**
         * buisCode : 70886837
         * createTime : 2016-07-06 13:52:10
         * createUser : 210001
         * curNode : 3
         * flowCode : AL-2016-001
         * flowStatus : 1
         * instId : 268141165688131584
         * nodeName : 校长室审批03
         * schoolId : 1
         * status : 1
         * taskGroup : 1
         * taskType : 0
         * taskUser : 260599263199760384
         * updateTime : 2016-07-06 15:23:36
         * updateUser : 260599263199760384
         */

        public OrderStateMsgBean orderStateMsg;
        public String schoolId;
        public int sma;
        public String startTime;
        public int subjectId;
        public String summary;
        public int teacherId;
        public int type;
        public int week;

        public static class OrderStateMsgBean  implements Serializable {
            public String buisCode;
            public String createTime;
            public int createUser;
            public int curNode;
            public String flowCode;
            public String flowStatus;
            public String instId;
            public String nodeName;
            public String schoolId;
            public String status;
            public int taskGroup;
            public int taskType;
            public long taskUser;
            public String updateTime;
            public long updateUser;
        }
    }
}
