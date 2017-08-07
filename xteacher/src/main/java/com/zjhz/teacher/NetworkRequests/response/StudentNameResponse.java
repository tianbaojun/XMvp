package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 学生当前位置查询   根据学生学号或姓名查询学生列表
 */
public class StudentNameResponse implements Serializable {


    /**
     * msg : 查询到1条记录!
     * code : 0
     * total : 1
     * data : [{"birthday":"2016-06-03 00:00:00","gradeId":"257187311769358336","idCard":"331082200704260010","certificateId":"991977290","sex":1,"gradeNo":"03","rfidCard":"G331082200704260010","studentId":"130105","classId":"25","createTime":"2016-06-08 10:33:32","schoolId":"1","classNo":"0301","name":"张麦口","createUser":1,"admissTime":"2001-09-01 00:00:00","status":"1"}]
     */

    public String msg;
    public int code;
    public int total;
    /**
     * birthday : 2016-06-03 00:00:00
     * gradeId : 257187311769358336
     * idCard : 331082200704260010
     * certificateId : 991977290
     * sex : 1
     * gradeNo : 03
     * rfidCard : G331082200704260010
     * studentId : 130105
     * classId : 25
     * createTime : 2016-06-08 10:33:32
     * schoolId : 1
     * classNo : 0301
     * name : 张麦口
     * createUser : 1
     * admissTime : 2001-09-01 00:00:00
     * status : 1
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String birthday;
        public String gradeId;
        public String idCard;
        public String certificateId;
        public int sex;
        public String gradeNo;
        public String rfidCard;
        public String studentId;
        public String classId;
        public String createTime;
        public String schoolId;
        public String classNo;
        public String name;
        public int createUser;
        public String admissTime;
        public String status;
    }
}
