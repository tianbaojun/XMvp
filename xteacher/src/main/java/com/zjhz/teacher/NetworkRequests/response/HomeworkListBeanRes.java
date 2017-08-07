package com.zjhz.teacher.NetworkRequests.response;

import android.text.TextUtils;

import com.zjhz.teacher.R;

import java.io.Serializable;

/**
 * Created by zzd on 2017/6/28.
 */

public class HomeworkListBeanRes implements Serializable {

    /**
     * classNameCont : 一（3）班、一（4）班、一（5）班、六（5）班等4个班级.
     * createUser : 288078455709372436
     * homeworkId : 395727587755692032
     * homeworkName : 陈国栋教师于23号发布英语科目作业
     * noSubmitNum : 250
     * photoUrl : http://www.1000xyun.com:80/group1/M00/00/26/CmUBelf9z5uAHrFXAAAhN6tochM920.jpg?attname=1476251575860.jpg
     * publishTime : 2017-06-23 00:00:00
     * status : 2
     * subjectId : 288073718670823424
     * submitNum : 0
     * teacherId : 288078455709372435
     * teacherName : 陈国栋(语文老师)
     * homeworkNature : SYS_HOMEWORK_TYPE_2
     * homeworkType : SYS_HOMEWORK_TYPE_3
     * homeworkTypeName : 图文结合
     */

    private String classNameCont;
    private String createUser;
    private String homeworkId;
    private String homeworkName;
    private int noSubmitNum;
    private String photoUrl;
    private String publishTime;
    private int status;//1 待发布 2 已发布 3 已完结
    private String subjectId;
    private int submitNum;
    private String teacherId;
    private String teacherName;
    private String homeworkNature;
    private String homeworkType;
    private String homeworkTypeName;


    private String statusString;
    private int statusIcon;
    private int statusColor;
    private int homeworkNatureIcon;

    public String getClassNameCont() {
        return classNameCont;
    }

    public void setClassNameCont(String classNameCont) {
        this.classNameCont = classNameCont;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public int getNoSubmitNum() {
        return noSubmitNum;
    }

    public void setNoSubmitNum(int noSubmitNum) {
        this.noSubmitNum = noSubmitNum;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(int submitNum) {
        this.submitNum = submitNum;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getHomeworkNature() {
        return homeworkNature;
    }

    public void setHomeworkNature(String homeworkNature) {
        this.homeworkNature = homeworkNature;
    }

    public String getHomeworkType() {
        return homeworkType;
    }

    public void setHomeworkType(String homeworkType) {
        this.homeworkType = homeworkType;
    }

    public String getHomeworkTypeName() {
        return homeworkTypeName;
    }

    public void setHomeworkTypeName(String homeworkTypeName) {
        this.homeworkTypeName = homeworkTypeName;
    }

    public String getStatusString() {
        switch (status){
            case 1:
                statusString = "待发布";
                break;
            case 2:
                statusString = "已发布";
                break;
            case 3:
                statusString = "已完结";
                break;
        }
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public int getStatusIcon() {
        switch (status){
            case 1:
                statusIcon = R.mipmap.homework_yfb;
                break;
            case 2:
                statusIcon = R.mipmap.homework_yfb;
                break;
            case 3:
                statusIcon = R.mipmap.homework_ywj;
                break;
        }
        return statusIcon;
    }

    public void setStatusIcon(int statusIcon) {
        this.statusIcon = statusIcon;
    }

    public int getStatusColor() {
        switch (status){
            case 1:
                statusColor = R.color.homework_yfb;
                break;
            case 2:
                statusColor = R.color.homework_yfb;
                break;
            case 3:
                statusColor = R.color.homework_ywj;
                break;
        }
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }

    public int getHomeworkNatureIcon() {
        if (TextUtils.isEmpty(homeworkNature)) {
            homeworkNatureIcon = R.mipmap.homework_a;
            return homeworkNatureIcon;
        }
        switch (homeworkNature){
            case "SYS_HOMEWORK_NATURE_1"://书面作业
                homeworkNatureIcon = R.mipmap.homework_b;
                break;
            case "SYS_HOMEWORK_NATURE_2"://电子作业作业
                homeworkNatureIcon = R.mipmap.homework_a;
                break;
        }
        return homeworkNatureIcon;
    }

    public void setHomeworkNatureIcon(int homeworkNatureIcon) {
        this.homeworkNatureIcon = homeworkNatureIcon;
    }
}
