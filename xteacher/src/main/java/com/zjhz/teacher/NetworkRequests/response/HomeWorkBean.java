package com.zjhz.teacher.NetworkRequests.response;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 *作业对象
 * Created by Administrator on 2016/6/25.
 */
public class HomeWorkBean implements Serializable{
    private String homeworkId= "";
    private String photoUrl = "";
    private String createUserName = "";
    private String subjectName = "";
    private String createUser = "";
    private String homeworkName= "";
    private String createTime= "";
    private String publishTime= "";
    private String deliverTime= "";
    private String costTime= "";
    private String content= "";
    private String subjectId= "";
    private String replyNum= "";
    private String teacherId = "";
    private String homeworkType = "";
    private String homeworkNature = "";
    private String homeworkString = "";
    private int shareFlag  ;

    private List<ImageBean> imgs;
    private List<ClassesBeans> classes;
    private List<ClassesBeans> link;
    private String classString = "";

    private boolean isShowName = true;

    public int getShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(int shareFlag) {
        this.shareFlag = shareFlag;
    }

    public String getClassString() {
        if(link == null || link.size() == 0)
            return null;
        for(ClassesBeans beans: link)
            classString += beans.getClassName()+"、";
        if(!TextUtils.isEmpty(classString)){
            classString = classString.substring(0, classString.length() - 1);
        }
        return classString;
    }

    public void setClassString(String classString) {
        this.classString = classString;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public String getReplyNum() {
        return replyNum;
    }

    private String praiseNum= "";

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<ClassesBeans> getLink() {
        return link;
    }

    public void setLink(List<ClassesBeans> link) {
        this.link = link;
    }

    public String getCostTime() {
        return costTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public List<ImageBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImageBean> imgs) {
        this.imgs = imgs;
    }

    public String getDeliverTime() {
        if(!TextUtils.isEmpty(deliverTime) && deliverTime.length() > 16){
            deliverTime = deliverTime.substring(0, 16);
        }
        return deliverTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public List<ClassesBeans> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassesBeans> classes) {
        this.classes = classes;
    }

    public String getHomeworkType() {
        return homeworkType;
    }

    public void setHomeworkType(String homeworkType) {
        this.homeworkType = homeworkType;
    }

    public String getHomeworkNature() {
        return homeworkNature;
    }

    public void setHomeworkNature(String homeworkNature) {
        this.homeworkNature = homeworkNature;
    }

    public String getHomeworkString() {
        switch (homeworkType){
            case "SYS_HOMEWORK_TYPE_1":
                homeworkString = "全文字";
                break;
            case "SYS_HOMEWORK_TYPE_2":
                homeworkString = "图片";
                break;
            case "SYS_HOMEWORK_TYPE_3":
                homeworkString = "图文结合";
                break;
            case "SYS_HOMEWORK_TYPE_4":
                homeworkString = "语音";
                break;
        }
        return homeworkString;
    }

    public void setHomeworkString(String homeworkString) {
        this.homeworkString = homeworkString;
    }

    public boolean isShowName() {
        if (TextUtils.isEmpty(homeworkNature)) {
            isShowName = true;
            return isShowName;
        }
        switch (homeworkNature){
            case "SYS_HOMEWORK_NATURE_1"://书面作业
                isShowName = false;
                break;
            case "SYS_HOMEWORK_NATURE_2"://电子作业作业
                isShowName = true;
                break;
        }
        return isShowName;
    }
}
