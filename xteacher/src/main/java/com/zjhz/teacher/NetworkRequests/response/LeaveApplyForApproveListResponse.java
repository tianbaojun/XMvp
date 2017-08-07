package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 审批申请列表  响应数据
 */
public class LeaveApplyForApproveListResponse implements Serializable {


    /**
     * code : 0
     * data : [{"applyer":14,"applyerTime":"2016-07-19 00:00:00","createTime":"2016-07-19 08:48:15","createUser":14,"ema":2,"endTime":"2016-07-19 00:00:00","flowStatus":"1","holidayId":"272775723301736448","oid":272775723301736448,"orderStateMsg":{"approveFlag":"2","buisCode":"272775723301736448","createTime":"2016-07-19 08:48:15","createUser":14,"curNode":4,"flowCode":"AL-2016-001","flowStatus":"0","instId":"272775723368845312","nodeName":"第4级审批","schoolId":"1","status":"0","taskGroup":9,"taskType":1,"taskUser":33,"updateTime":"2016-07-19 10:12:37","updateUser":16},"schoolId":"1","sma":1,"startTime":"2016-07-19 00:00:00","summary":"我要结婚了","type":1}]
     * msg : 待审批的列加查询成功!
     * total : 1
     */

    public int code;
    public String msg;
    public int total;
    /**
     * applyer : 14
     * applyerTime : 2016-07-19 00:00:00
     * createTime : 2016-07-19 08:48:15
     * createUser : 14
     * ema : 2
     * endTime : 2016-07-19 00:00:00
     * flowStatus : 1
     * holidayId : 272775723301736448
     * oid : 272775723301736448
     * orderStateMsg : {"approveFlag":"2","buisCode":"272775723301736448","createTime":"2016-07-19 08:48:15","createUser":14,"curNode":4,"flowCode":"AL-2016-001","flowStatus":"0","instId":"272775723368845312","nodeName":"第4级审批","schoolId":"1","status":"0","taskGroup":9,"taskType":1,"taskUser":33,"updateTime":"2016-07-19 10:12:37","updateUser":16}
     * schoolId : 1
     * sma : 1
     * startTime : 2016-07-19 00:00:00
     * summary : 我要结婚了
     * type : 1
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable  {
        public int applyer;
        public String applyerTime;
        public String createTime;
        public int createUser;
        public int ema;
        public String endTime;
        public String flowStatus;
        public String holidayId;
        public long oid;
        /**
         * approveFlag : 2
         * buisCode : 272775723301736448
         * createTime : 2016-07-19 08:48:15
         * createUser : 14
         * curNode : 4
         * flowCode : AL-2016-001
         * flowStatus : 0
         * instId : 272775723368845312
         * nodeName : 第4级审批
         * schoolId : 1
         * status : 0
         * taskGroup : 9
         * taskType : 1
         * taskUser : 33
         * updateTime : 2016-07-19 10:12:37
         * updateUser : 16
         */

        public OrderStateMsgBean orderStateMsg;
        public String schoolId;
        public int sma;
        public String startTime;
        public String summary;
        public int type;

        public static class OrderStateMsgBean implements Serializable {
            public String approveFlag;
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
            public String updateTime;
            public int updateUser;
        }
    }
}
