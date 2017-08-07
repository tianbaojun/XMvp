package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表查询教师名称分页
 */
public class MyScheduleQueryNamePagingResponse implements Serializable {
    /**
     * msg : 查询有效课表信息成功
     * code : 0
     * total : 15
     * data : [{"gradeName":"二年级","gradeId":"271706886842093568","week":"2","teacherName":"张兰芳","detailId":"274330988153147392","className":"二（3）班","subjectId":"271727907259289600","classId":"272819973980688384","teacherId":"272484438888157184","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"张兰芳","createUser":"272484438246428672","clazz":"1","scheduleId":"273870803802525696","status":"1","subjectName":"英语"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"3","teacherName":"张兰芳","detailId":"274330988165730304","updateUser":"272484438485504000","className":"二（3）班","updateTime":"2016-07-23 17:59:16","subjectId":"271727907259289600","classId":"272819973980688384","teacherId":"272484438888157184","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"张兰芳","createUser":"272484438246428672","clazz":"1","scheduleId":"273870803802525696","status":"1","subjectName":"英语"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"4","teacherName":"张兰芳","detailId":"274330988174118912","className":"二（3）班","subjectId":"271727907259289600","classId":"272819973980688384","teacherId":"272484438888157184","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"张兰芳","createUser":"272484438246428672","clazz":"1","scheduleId":"273870803802525696","status":"1","subjectName":"英语"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"5","teacherName":"张兰芳","detailId":"274330988186701824","className":"二（3）班","subjectId":"271727907259289600","classId":"272819973980688384","teacherId":"272484438888157184","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"张兰芳","createUser":"272484438246428672","clazz":"1","scheduleId":"273870803802525696","status":"1","subjectName":"英语"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"1","teacherName":"应聪玲","detailId":"274330988207673344","className":"二（3）班","subjectId":"271727905938083840","classId":"272819973980688384","teacherId":"272484438867185664","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"应聪玲","createUser":"272484438246428672","clazz":"2","scheduleId":"273870803802525696","status":"1","subjectName":"数学"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"2","teacherName":"崔仙丽","detailId":"274330988216061952","className":"二（3）班","subjectId":"271727905615122432","classId":"272819973980688384","teacherId":"272484438846214144","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"崔仙丽","createUser":"272484438246428672","clazz":"2","scheduleId":"273870803802525696","status":"1","subjectName":"语文"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"4","teacherName":"崔仙丽","detailId":"274330988237033472","className":"二（3）班","subjectId":"271727905615122432","classId":"272819973980688384","teacherId":"272484438846214144","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"崔仙丽","createUser":"272484438246428672","clazz":"2","scheduleId":"273870803802525696","status":"1","subjectName":"语文"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"5","teacherName":"崔仙丽","detailId":"274330988245422080","className":"二（3）班","subjectId":"271727905615122432","classId":"272819973980688384","teacherId":"272484438846214144","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"崔仙丽","createUser":"272484438246428672","clazz":"2","scheduleId":"273870803802525696","status":"1","subjectName":"语文"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"1","teacherName":"张兰芳","detailId":"274330988262199296","className":"二（3）班","subjectId":"271727907259289600","classId":"272819973980688384","teacherId":"272484438888157184","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"张兰芳","createUser":"272484438246428672","clazz":"3","scheduleId":"273870803802525696","status":"1","subjectName":"英语"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"2","teacherName":"应聪玲","detailId":"274330988274782208","className":"二（3）班","subjectId":"271727905938083840","classId":"272819973980688384","teacherId":"272484438867185664","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"应聪玲","createUser":"272484438246428672","clazz":"3","scheduleId":"273870803802525696","status":"1","subjectName":"数学"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"3","teacherName":"应聪玲","detailId":"274330988283170816","className":"二（3）班","subjectId":"271727905938083840","classId":"272819973980688384","teacherId":"272484438867185664","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"应聪玲","createUser":"272484438246428672","clazz":"3","scheduleId":"273870803802525696","status":"1","subjectName":"数学"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"4","teacherName":"应聪玲","detailId":"274330988295753728","className":"二（3）班","subjectId":"271727905938083840","classId":"272819973980688384","teacherId":"272484438867185664","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"应聪玲","createUser":"272484438246428672","clazz":"3","scheduleId":"273870803802525696","status":"1","subjectName":"数学"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"5","teacherName":"应聪玲","detailId":"274330988304142336","className":"二（3）班","subjectId":"271727905938083840","classId":"272819973980688384","teacherId":"272484438867185664","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"应聪玲","createUser":"272484438246428672","clazz":"3","scheduleId":"273870803802525696","status":"1","subjectName":"数学"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"1","teacherName":"何礼忠","detailId":"274330988316725248","className":"二（3）班","subjectId":"271727908362391552","classId":"272819973980688384","teacherId":"272484438284177408","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"何礼忠","createUser":"272484438246428672","clazz":"4","scheduleId":"273870803802525696","status":"1","subjectName":"体育"},{"gradeName":"二年级","gradeId":"271706886842093568","week":"2","teacherName":"冯丹红","detailId":"274330988325113856","className":"二（3）班","subjectId":"271727907951349760","classId":"272819973980688384","teacherId":"272484438363869184","createTime":"2016-07-23 15:48:18","schoolId":"271688204724211712","name":"冯丹红","createUser":"272484438246428672","clazz":"4","scheduleId":"273870803802525696","status":"1","subjectName":"地理"}]
     */

    private String msg;
    private int code;
    private int total;
    /**
     * gradeName : 二年级
     * gradeId : 271706886842093568
     * week : 2
     * teacherName : 张兰芳
     * detailId : 274330988153147392
     * className : 二（3）班
     * subjectId : 271727907259289600
     * classId : 272819973980688384
     * teacherId : 272484438888157184
     * createTime : 2016-07-23 15:48:18
     * schoolId : 271688204724211712
     * name : 张兰芳
     * createUser : 272484438246428672
     * clazz : 1
     * scheduleId : 273870803802525696
     * status : 1
     * subjectName : 英语
     */

    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private String gradeName;
        private String gradeId;
        private String week;
        private String teacherName;
        private String detailId;
        private String className;
        private String subjectId;
        private String classId;
        private String teacherId;
        private String createTime;
        private String schoolId;
        private String name;
        private String createUser;
        private String clazz;
        private String scheduleId;
        private String status;
        private String subjectName;

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getDetailId() {
            return detailId;
        }

        public void setDetailId(String detailId) {
            this.detailId = detailId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        public String getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }
    }
}
