package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/13.
 */

public class CompanyNewsBean implements Serializable {

    /**
     * summary : 这是小明第3次请小红吃饭！
     * publishTime : 2017-06-09 13:29:10
     * headImg : http://msg.51jxh.com:226/group1/M00/00/07/CmUBaVd0p9iAUFupAABCL9a5QOw974.jpg?attname=339.jpg
     * praiseNum : 0
     * updateTime : 2017-06-09 13:29:10
     * title : 小红和小明去约会
     * content : 2017年7月1号，清风徐徐。小明约上小红一起去肯德基吃了全家桶。~，~
     * newsId : 388023872244420608
     * createTime : 2017-06-02 10:44:53
     * replyNum : 4
     * createUser : 288078455667429377
     * categoryId : 0
     * lookNum : 78
     * status : 3
     * publishUser : 272519939070365696
     * updateUser : 272519939070365696
     */

    private String summary;
    private String publishTime;
    private String headImg;
    private int praiseNum;
    private String updateTime;
    private String title;
    private String content;
    private String newsId;
    private String createTime;
    private int replyNum;
    private String createUser;
    private String categoryId;
    private int lookNum;
    private String status;
    private String publishUser;
    private String updateUser;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
