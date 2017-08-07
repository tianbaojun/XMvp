package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 学生当前位置查询   根据班级年级查询学生列表
 */
public class GradeClassResponse implements Serializable{


    /**
     * code : 0
     * data : [{"admissTime":"2008-09-11 00:00:00","birthday":"2003-04-20 00:00:00","certificateId":"3410145479","classId":"272777947025248256","className":"一（1）班","createTime":"2016-07-21 13:26:36","createUser":0,"folkCode":432,"gradeId":"271706886791761920","gradeName":"一年级","graduationTime":"2014-06-11 00:00:00","idCard":"330682199001153039","name":"陈哲杰","nativePlaceCode":446,"politicsCode":454,"residenceAddress":"浙江","residencePrope":0,"residenceType":409,"rfidCard":"0602153050011","schoolId":"271688204724211712","sex":1,"sourceType":567,"status":"1","studentId":"273570550394130432","summary":"无","updateTime":"2016-07-23 11:27:43","updateUser":271688204749377536},{"admissTime":"2008-09-12 00:00:00","birthday":"2003-05-15 00:00:00","certificateId":"3410272759","classId":"272777947025248256","className":"一（1）班","createTime":"2016-07-21 13:26:36","createUser":0,"folkCode":1,"gradeId":"271706886791761920","gradeName":"一年级","graduationTime":"2014-06-12 00:00:00","idCard":"330602200305156523","name":"章佳意","nativePlaceCode":1,"politicsCode":3,"residenceAddress":"浙江","residenceType":1,"rfidCard":"0602153050012","schoolId":"271688204724211712","sex":2,"sourceType":1,"status":"1","studentId":"273570550440267776","summary":"无","updateTime":"2016-07-22 19:03:56","updateUser":0},{"admissTime":"2008-09-13 00:00:00","birthday":"2003-02-26 00:00:00","certificateId":"1695766359","classId":"272777947025248256","className":"一（1）班","createTime":"2016-07-21 13:26:36","createUser":0,"folkCode":1,"gradeId":"271706886791761920","gradeName":"一年级","graduationTime":"2014-06-13 00:00:00","idCard":"330602200302266516","name":"陆琦栋","nativePlaceCode":1,"politicsCode":3,"residenceAddress":"浙江","residenceType":1,"rfidCard":"0602153050013","schoolId":"271688204724211712","sex":1,"sourceType":1,"status":"1","studentId":"273570550465433600","summary":"无","updateTime":"2016-07-22 19:08:03","updateUser":0},{"admissTime":"2008-09-14 00:00:00","birthday":"2003-09-03 00:00:00","certificateId":"3410222423","classId":"272777947025248256","className":"一（1）班","createTime":"2016-07-21 13:26:36","createUser":0,"folkCode":1,"gradeId":"271706886791761920","gradeName":"一年级","graduationTime":"2014-06-14 00:00:00","idCard":"330602200309036529","name":"冯诗怡","nativePlaceCode":1,"politicsCode":3,"residenceAddress":"浙江","residenceType":1,"schoolId":"271688204724211712","sex":2,"sourceType":1,"status":"1","studentId":"273570550494793728","summary":"无"},{"admissTime":"2008-09-15 00:00:00","birthday":"2003-07-07 00:00:00","certificateId":"3410270167","classId":"272777947025248256","className":"一（1）班","createTime":"2016-07-21 13:26:36","createUser":0,"folkCode":1,"gradeId":"271706886791761920","gradeName":"一年级","graduationTime":"2014-06-15 00:00:00","idCard":"330602200307076551","name":"朱佳超","nativePlaceCode":1,"politicsCode":3,"residenceAddress":"浙江","residenceType":1,"schoolId":"271688204724211712","sex":1,"sourceType":1,"status":"1","studentId":"273570550519959552","summary":"无"}]
     * msg : 查询到10条记录!
     * total : 10
     */

    public int code;
    public String msg;
    public int total;
    /**
     * admissTime : 2008-09-11 00:00:00
     * birthday : 2003-04-20 00:00:00
     * certificateId : 3410145479
     * classId : 272777947025248256
     * className : 一（1）班
     * createTime : 2016-07-21 13:26:36
     * createUser : 0
     * folkCode : 432
     * gradeId : 271706886791761920
     * gradeName : 一年级
     * graduationTime : 2014-06-11 00:00:00
     * idCard : 330682199001153039
     * name : 陈哲杰
     * nativePlaceCode : 446
     * politicsCode : 454
     * residenceAddress : 浙江
     * residencePrope : 0
     * residenceType : 409
     * rfidCard : 0602153050011
     * schoolId : 271688204724211712
     * sex : 1
     * sourceType : 567
     * status : 1
     * studentId : 273570550394130432
     * summary : 无
     * updateTime : 2016-07-23 11:27:43
     * updateUser : 271688204749377536
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable{
        public String admissTime;
        public String birthday;
        public String certificateId;
        public String classId;
        public String className;
        public String createTime;
        public String createUser;
        public int folkCode;
        public String gradeId;
        public String gradeName;
        public String graduationTime;
        public String idCard;
        public String name;
        public int nativePlaceCode;
        public int politicsCode;
        public String residenceAddress;
        public int residencePrope;
        public int residenceType;
        public String rfidCard;
        public String schoolId;
        public int sex;
        public int sourceType;
        public String status;
        public String studentId;
        public String summary;
        public String updateTime;
        public long updateUser;
    }
}
