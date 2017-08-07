package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 新闻
 * Created by xiangxue on 2016/6/8.
 */
public class NewsBean implements Serializable{
    private String newsId = "";
    private String title = "";
    private String summary = "";
    private String publishUserVal = "";
    private String publishTime = "";
    private String content = "";
    private String headImg = "";
    private String createTime = "";
    private String createUser = "";
    private int praiseNum = 0;
    private int replyNum = 0;
    private String name = "";
    private String createUserVal = "";
    private boolean isPraise = false;
    private int lookNum = 0;
    private String praiseId = "";

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishUserVal() {
        return publishUserVal;
    }

    public void setPublishUserVal(String publishUserVal) {
        this.publishUserVal = publishUserVal;
    }

    public String getCreateUserVal() {
        return createUserVal;
    }

    public void setCreateUserVal(String createUserVal) {
        this.createUserVal = createUserVal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
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

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
}
