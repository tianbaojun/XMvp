package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tabjin on 2017/4/13.
 * Description:
 * What Changed:
 */

public class ClasszMoment implements Serializable {
    /**
     * `dynamic_type`  '动态类型 0 文字 1 图片 2 视频 3语音'
     */
    private String publishTime,dynamicType, classId, dcId, createTime, ccdContent, schoolId, dcCode, describes, dId, publishName, publishUser, photoUrl, ccdTitle, keywords, currentUserPraiseStatus, keywordsValue;
    private List<ImageUrl> imageUrls;
    private List<ClasszMomentCommentBean> listComment;
    private List<ClasszMomentPariseBean> listPraise;
    private List<ClasszMomentDocumentAttachment> listAttachment;

    /**班级圈动态 type：
        0	原生
        1 	成长日记
        2	电子作业
    */
    private String type = "";
    private String parentId;

    private String homeworkName;
    private String praiseLevelName;
    private String praiseContent;
    private String praiseLevel;
    private String content;

    private String pypf;

    private boolean isOpen;

    public void setDynamicType(String dynamicType) {
        this.dynamicType = dynamicType;
    }

    public String getDynamicType() {
        return dynamicType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public String getPraiseLevelName() {
        return praiseLevelName;
    }

    public void setPraiseLevelName(String praiseLevelName) {
        this.praiseLevelName = praiseLevelName;
    }

    public String getPraiseContent() {
        return praiseContent;
    }

    public void setPraiseContent(String praiseContent) {
        this.praiseContent = praiseContent;
    }

    public String getPraiseLevel() {
        return praiseLevel;
    }

    public void setPraiseLevel(String praiseLevel) {
        this.praiseLevel = praiseLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPypf() {
        return pypf;
    }

    public void setPypf(String pypf) {
        this.pypf = pypf;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCurrentUserPraiseStatus() {
        return currentUserPraiseStatus;
    }

    public void setCurrentUserPraiseStatus(String currentUserPraiseStatus) {
        this.currentUserPraiseStatus = currentUserPraiseStatus;
    }

    public String getKeywordsValue() {
        return keywordsValue;
    }

    public void setKeywordsValue(String keywordsValue) {
        this.keywordsValue = keywordsValue;
    }

    public List<ClasszMomentDocumentAttachment> getListAttachment() {
        return listAttachment;
    }

    public void setListAttachment(List<ClasszMomentDocumentAttachment> listAttachment) {
        this.listAttachment = listAttachment;
    }

    public String getCcdTitle() {
        return ccdTitle;
    }

    public void setCcdTitle(String ccdTitle) {
        this.ccdTitle = ccdTitle;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<ClasszMomentCommentBean> getListComment() {
        return listComment;
    }

    public void setListComment(List<ClasszMomentCommentBean> listComment) {
        this.listComment = listComment;
    }

    public List<ClasszMomentPariseBean> getListPraise() {
        return listPraise;
    }

    public void setListPraise(List<ClasszMomentPariseBean> listPraise) {
        this.listPraise = listPraise;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCcdContent() {
        return ccdContent;
    }

    public void setCcdContent(String ccdContent) {
        this.ccdContent = ccdContent;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getDcCode() {
        return dcCode;
    }

    public void setDcCode(String dcCode) {
        this.dcCode = dcCode;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    public List<ImageUrl> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageUrl> imageUrls) {
        this.imageUrls = imageUrls;
    }


}
