package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * 食谱对象
 * Created by xiangxue on 2016/6/12.
 */
public class FoodBean implements Serializable{
    private String cookbookId = "";
    private String createTime = "";
    private String publishTime = "";
    private String createUser = "";
    private String content ="";
    private int pattern = -1;
    private List<ImageBean> imageUrls;
    private int replyNum = 0;
    private int praiseNum = 0;
    private int lookNum = 0 ;
    private boolean isPraise;
    private String praiseId;
    private String photoUrl;
    private String name;

    private String title = "";
    private int status = 0;
    private long schoolId = 0;
    private String updateUser = "";

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPraiseId() {
        return praiseId;
    }

    public void setPraiseId(String praiseId) {
        this.praiseId = praiseId;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public List<ImageBean> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageBean> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getCookbookId() {
        return cookbookId;
    }

    public void setCookbookId(String cookbookId) {
        this.cookbookId = cookbookId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public int getPattern() {
        return pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
