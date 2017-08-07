package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 请假申请详情  相应数据
 *
 */
public class LeaveApplyForStateResponse implements Serializable {


    /**
     * code : 0
     * data : {"applyer":2300003,"applyerTime":"2016-04-08 00:00:00","clazz":2,"createTime":"2016-07-06 00:00:00","createUser":-1,"ema":1,"endTime":"2016-04-08 12:40:44","oid":268186733042995200,"schoolId":"1","sma":0,"startTime":"2016-04-08 12:40:44","subjectId":12,"summary":"测试工作组审批reject流程","taskList":[{"buisCode":"268186733042995200","content":"init","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 16:54:11","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"0","taskGroup":551001,"taskId":"268186973510832128","taskType":2,"taskUser":2300003},{"buisCode":"268186733042995200","content":"当前部门带回退审批1通过","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:03:47","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"1","taskGroup":551001,"taskId":"268189386359705600","taskType":2,"taskUser":5510001},{"buisCode":"268186733042995200","content":"init","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:03:47","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"0","taskGroup":552001,"taskId":"268189386435203072","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"当前工作组带回退审批2通过","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:05:05","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"1","taskGroup":552001,"taskId":"268189715608375296","taskType":2,"taskUser":5520001},{"buisCode":"268186733042995200","content":"init","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:05:05","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"0","taskGroup":553001,"taskId":"268189715713232896","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"当前工作组审批3不通过，修改重新提交!","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:05:58","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"2","taskGroup":553001,"taskId":"268189935721254912","taskType":2,"taskUser":5530001},{"buisCode":"268186733042995200","content":"工作组回退，修改了重新提交","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:07:20","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"0","taskGroup":551001,"taskId":"268190282934128640","taskType":2,"taskUser":2300003},{"buisCode":"268186733042995200","content":"reject当前工作组用户审批1通过","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:09:17","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"1","taskGroup":551001,"taskId":"268190771708956672","taskType":2,"taskUser":5510001},{"buisCode":"268186733042995200","content":"init","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:09:17","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"0","taskGroup":552001,"taskId":"268190771977392128","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"reject当前工作组审批2通过","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:10:00","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"1","taskGroup":552001,"taskId":"268190950981898240","taskType":2,"taskUser":5520001},{"buisCode":"268186733042995200","content":"init","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:10:00","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"0","taskGroup":553001,"taskId":"268190951229362176","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"reject当前用户审批3通过","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:10:32","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"1","taskGroup":553001,"taskId":"268191084570480640","taskType":2,"taskUser":5530002}],"teacherId":1,"type":1,"week":1}
     * msg :
     */

    public int code;
    /**
     * applyer : 2300003
     * applyerTime : 2016-04-08 00:00:00
     * clazz : 2
     * createTime : 2016-07-06 00:00:00
     * createUser : -1
     * ema : 1
     * endTime : 2016-04-08 12:40:44
     * oid : 268186733042995200
     * schoolId : 1
     * sma : 0
     * startTime : 2016-04-08 12:40:44
     * subjectId : 12
     * summary : 测试工作组审批reject流程
     * taskList : [{"buisCode":"268186733042995200","content":"init","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 16:54:11","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"0","taskGroup":551001,"taskId":"268186973510832128","taskType":2,"taskUser":2300003},{"buisCode":"268186733042995200","content":"当前部门带回退审批1通过","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:03:47","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"1","taskGroup":551001,"taskId":"268189386359705600","taskType":2,"taskUser":5510001},{"buisCode":"268186733042995200","content":"init","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:03:47","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"0","taskGroup":552001,"taskId":"268189386435203072","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"当前工作组带回退审批2通过","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:05:05","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"1","taskGroup":552001,"taskId":"268189715608375296","taskType":2,"taskUser":5520001},{"buisCode":"268186733042995200","content":"init","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:05:05","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"0","taskGroup":553001,"taskId":"268189715713232896","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"当前工作组审批3不通过，修改重新提交!","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:05:58","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"2","taskGroup":553001,"taskId":"268189935721254912","taskType":2,"taskUser":5530001},{"buisCode":"268186733042995200","content":"工作组回退，修改了重新提交","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:07:20","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"0","taskGroup":551001,"taskId":"268190282934128640","taskType":2,"taskUser":2300003},{"buisCode":"268186733042995200","content":"reject当前工作组用户审批1通过","curNode":1,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:09:17","instId":"268186973397585920","nodeName":"工作组2用户1审批","schoolId":"1","status":"1","taskGroup":551001,"taskId":"268190771708956672","taskType":2,"taskUser":5510001},{"buisCode":"268186733042995200","content":"init","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:09:17","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"0","taskGroup":552001,"taskId":"268190771977392128","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"reject当前工作组审批2通过","curNode":2,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:10:00","instId":"268186973397585920","nodeName":"工作组2用户2审批","schoolId":"1","status":"1","taskGroup":552001,"taskId":"268190950981898240","taskType":2,"taskUser":5520001},{"buisCode":"268186733042995200","content":"init","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:10:00","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"0","taskGroup":553001,"taskId":"268190951229362176","taskType":2,"taskUser":0},{"buisCode":"268186733042995200","content":"reject当前用户审批3通过","curNode":3,"flowCode":"AL-2016-021","flowTime":"2016-07-06 17:10:32","instId":"268186973397585920","nodeName":"工作组2用户3审批","schoolId":"1","status":"1","taskGroup":553001,"taskId":"268191084570480640","taskType":2,"taskUser":5530002}]
     * teacherId : 1
     * type : 1
     * week : 1
     */

    public DataBean data;
    public String msg;

    public static class DataBean implements Serializable {
        public int applyer;
        public String applyerTime;
        public int clazz;
        public String createTime;
        public int createUser;
        public int ema;
        public String endTime;
        public long oid;
        public String schoolId;
        public int sma;
        public String startTime;
        public int subjectId;
        public String summary;
        public int teacherId;
        public int type;
        public int week;
        /**
         * buisCode : 268186733042995200
         * content : init
         * curNode : 1
         * flowCode : AL-2016-021
         * flowTime : 2016-07-06 16:54:11
         * instId : 268186973397585920
         * nodeName : 工作组2用户1审批
         * schoolId : 1
         * status : 0
         * taskGroup : 551001
         * taskId : 268186973510832128
         * taskType : 2
         * taskUser : 2300003
         */

        public List<TaskListBean> taskList;

        public static class TaskListBean implements Serializable {
            public String buisCode;
            public String content;
            public int curNode;
            public String flowCode;
            public String flowTime;
            public String instId;
            public String nodeName;
            public String schoolId;
            public String status;
            public int taskGroup;
            public String taskId;
            public int taskType;
            public int taskUser;
        }
    }
}
