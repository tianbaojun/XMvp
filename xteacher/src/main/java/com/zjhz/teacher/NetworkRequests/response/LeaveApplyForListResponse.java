package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表  响应数据
 */
public class LeaveApplyForListResponse implements Serializable {


    /**
     * code : 0
     * data : [{"applyer":15,"applyerTime":"2016-07-19 00:00:00","clazz":1,"createTime":"2016-07-19 10:36:45","createUser":15,"ema":2,"endTime":"2016-01-03 00:00:00","flowStatus":"1","holidayId":"272803027881037824","oid":272803027881037824,"orderFlowState":{"buisCode":"272803027881037824","createTime":"2016-07-19 10:36:45","createUser":15,"curNode":1,"flowCode":"AL-2016-001","flowStatus":"0","instId":"272803027914592256","nodeName":"第1级审批","schoolId":"1","status":"0","taskGroup":0,"taskType":0,"taskUser":14},"schoolId":"1","sma":1,"startTime":"2016-01-01 00:00:00","subjectId":1,"summary":"肿么","teacherId":1,"type":1,"week":1}]
     * msg : 查询到1条记录!
     * total : 1
     */

    public int code;
    public String msg;
    public int total;
    /**
     * applyer : 15
     * applyerTime : 2016-07-19 00:00:00
     * clazz : 1
     * createTime : 2016-07-19 10:36:45
     * createUser : 15
     * ema : 2
     * endTime : 2016-01-03 00:00:00
     * flowStatus : 1
     * holidayId : 272803027881037824
     * oid : 272803027881037824
     * orderFlowState : {"buisCode":"272803027881037824","createTime":"2016-07-19 10:36:45","createUser":15,"curNode":1,"flowCode":"AL-2016-001","flowStatus":"0","instId":"272803027914592256","nodeName":"第1级审批","schoolId":"1","status":"0","taskGroup":0,"taskType":0,"taskUser":14}
     * schoolId : 1
     * sma : 1
     * startTime : 2016-01-01 00:00:00
     * subjectId : 1
     * summary : 肿么
     * teacherId : 1
     * type : 1
     * week : 1
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable {
        public int applyer;
        public String applyerTime;
        public int clazz;
        public String createTime;
        public int createUser;
        public int ema;
        public String endTime;
        public String flowStatus;
        public String holidayId;
        public long oid;
        /**
         * buisCode : 272803027881037824
         * createTime : 2016-07-19 10:36:45
         * createUser : 15
         * curNode : 1
         * flowCode : AL-2016-001
         * flowStatus : 0
         * instId : 272803027914592256
         * nodeName : 第1级审批
         * schoolId : 1
         * status : 0
         * taskGroup : 0
         * taskType : 0
         * taskUser : 14
         */

        public OrderFlowStateBean orderFlowState;
        public String schoolId;
        public int sma;
        public String startTime;
        public int subjectId;
        public String summary;
        public int teacherId;
        public int type;
        public int week;

        public static class OrderFlowStateBean implements Serializable {
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
            public int taskUser;
        }
    }
}
