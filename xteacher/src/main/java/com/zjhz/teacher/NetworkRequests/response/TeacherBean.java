package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

/**
 * 教师类
 * Created by Administrator on 2016/6/29.
 */
public class TeacherBean implements Serializable ,Comparable{
    private String nickName= "";
    private String phoneNo= "";
    private String userId = "";
    private String teacherId = "";
    private boolean isChecd = false;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public boolean isChecd() {
        return isChecd;
    }

    public void setChecd(boolean checd) {
        isChecd = checd;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public int compareTo(Object another) {
        TeacherBean teacherBean2 = (TeacherBean)another;
        Collator ca = Collator.getInstance(Locale.CHINA);
        return ca.compare(this.nickName,teacherBean2.nickName);
    }
}
