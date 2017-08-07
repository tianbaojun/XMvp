package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by zzd on 2017/4/14.
 */

public class VisitorBean implements Serializable {

    public String visitorName; //姓名
    public String visitorPhone;//电话
    public String visitorNum;//人数
    public String date;//列表拜访日期
    public String time;//列表拜访时间
    public String appointmentTime;//预约时间
    public String visitorOrg;//拜访单位
    public String reason;//拜访事由
    public String realTime;//实际拜访时间
    public String verifyCode;//拜访验证码
    public String leaveTime;//离校时间
    public String scanCodeEnterTime;//发卡入校时间
    public String scanCodeLeaveTime;//扫卡离校时间
    public String identityCode;//入校卡片号码
    public String verificationCode;//验证码
    public boolean isChecked = false;
    public String teacherName;

    //    public VisitState stateEnum;
    public int visitStatus = -1;// 1待拜访;2拜访中;3拜访完成;4系统关闭

    //    public boolean isTearcher;//是否是老师，（老师或者保安）
    public String recordId;

//    public enum VisitState {
//        ToVisit,//待拜访
//        InVisit,//拜访中
//        Complete//拜访完成
//    }


    //    "recordId": "377171199068737536",
//    "reason": "Play",
//    "STATUS": "1",
//    "name": "123",
//    "visitorOrg": "School",
//    "teacherId": "288078455692595210",
//    "appointmentTime": "2017-05-03 10:38:00",
//    "createTime": "2017-05-03 10:38:36",
//    "schoolId": "288069341826519040",
//    "createUser": "288078455692595210",
//    "visitorPhone": "15164055568",
//    "visitorNum": 2
    public String STATUS;
    public String teacherId;
    public String createTime;
    public String schoolId;
    public String createUser;

    public String getStateName() {
        switch (visitStatus) {
            case 0:
                return "草稿";
            case 1:
                return "待拜访";
            case 2:
                return "拜访中";
            case 3:
                return "拜访完成";
            case 4:
                return "系统关闭";

        }
        return "";
    }
}
