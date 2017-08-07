package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表查询教师进入分页
 */
public class MyScheduleQueryPagingResponse implements Serializable {

    /**
     * code : 0
     * data : [{"birthday":"1971-04-02","certifiedNo":"201601007","createTime":"2016-07-18 13:30:47","createUser":"0","deptId":"272792856349904896","deptName":"教导处","eduLevel":"472","folkCode":"432","graduateSchool":"师范","graduateTime":"1990-02-09","idCard":"220201199011086022","jobNumber":"335222","jobTitle":"494","joinTime":"1990-02-19","mainSubject":"271727907951349760","mainSubjectName":"地理","major":"教育","marriageStatus":"479","name":"冯丹红","nativePlaceCode":"446","orginType":"483","visitorPhone":"14511111117","politicsCode":"454","positionalTitle":"494","residenceAddress":"浙江","rowSort":1,"schoolId":"271688204724211712","sex":"BASE_STUDENT_SEX_2","sexVal":"女","status":"1","summary":"无","teacherId":"272484438363869184","updateTime":"2016-08-26 10:20:15","updateUser":"272484438368063488","userId":"272484438368063488"}]
     * msg : 查询到60条记录!
     * total : 60
     */

    public int code;
    public String msg;
    public int total;
    /**
     * birthday : 1971-04-02
     * certifiedNo : 201601007
     * createTime : 2016-07-18 13:30:47
     * createUser : 0
     * deptId : 272792856349904896
     * deptName : 教导处
     * eduLevel : 472
     * folkCode : 432
     * graduateSchool : 师范
     * graduateTime : 1990-02-09
     * idCard : 220201199011086022
     * jobNumber : 335222
     * jobTitle : 494
     * joinTime : 1990-02-19
     * mainSubject : 271727907951349760
     * mainSubjectName : 地理
     * major : 教育
     * marriageStatus : 479
     * name : 冯丹红
     * nativePlaceCode : 446
     * orginType : 483
     * visitorPhone : 14511111117
     * politicsCode : 454
     * positionalTitle : 494
     * residenceAddress : 浙江
     * rowSort : 1
     * schoolId : 271688204724211712
     * sex : BASE_STUDENT_SEX_2
     * sexVal : 女
     * status : 1
     * summary : 无
     * teacherId : 272484438363869184
     * updateTime : 2016-08-26 10:20:15
     * updateUser : 272484438368063488
     * userId : 272484438368063488
     */

    public List<DataBean> data;

    public static class DataBean implements Serializable {
        public String birthday;
        public String certifiedNo;
        public String createTime;
        public String createUser;
        public String deptId;
        public String deptName;
        public String eduLevel;
        public String folkCode;
        public String graduateSchool;
        public String graduateTime;
        public String idCard;
        public String jobNumber;
        public String jobTitle;
        public String joinTime;
        public String mainSubject;
        public String mainSubjectName;
        public String major;
        public String marriageStatus;
        public String name;
        public String nativePlaceCode;
        public String orginType;
        public String phone;
        public String politicsCode;
        public String positionalTitle;
        public String residenceAddress;
        public int rowSort;
        public String schoolId;
        public String sex;
        public String sexVal;
        public String status;
        public String summary;
        public String teacherId;
        public String updateTime;
        public String updateUser;
        public String userId;
    }
}
