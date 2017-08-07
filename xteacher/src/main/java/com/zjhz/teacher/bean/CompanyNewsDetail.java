package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 */

public class CompanyNewsDetail implements Serializable {

    /**
     * categoryId : 0
     * content : 2017年6月1号，清风徐徐。小明约上小红一起去华莱士吃了全家桶。~，~
     * createTime : 2017-06-01 15:20:30
     * createUser : 272484438485504000
     * headImg : http://msg.51jxh.com:226/group1/M00/00/07/CmUBaVd0p9iAUFupAABCL9a5QOw974.jpg?attname=339.jpg
     * list : [{"createTime":"2017-06-05 11:42:17","createUser":"288078455667429377","deleteFlag":1,"newsId":"387751390883090432","photoUrl":"http://file.1000xyun.com:81/group1/M00/00/09/wKgDZ1kTyTuAZLrSAABUNV_Omaw112.jpg?attname=0.jpg","praiseNum":0,"praiseStatus":0,"replyContent":"哈哈，白痴小明3","replyId":"389146027094970368","replyNum":0,"replyType":0,"schoolId":"288069341826519040","userName":"章银平"},{"createTime":"2017-06-05 11:42:04","createUser":"288078455667429377","deleteFlag":1,"newsId":"387751390883090432","photoUrl":"http://file.1000xyun.com:81/group1/M00/00/09/wKgDZ1kTyTuAZLrSAABUNV_Omaw112.jpg?attname=0.jpg","praiseNum":0,"praiseStatus":0,"replyContent":"哈哈，白痴小明1","replyId":"389145970673192960","replyNum":3,"replyType":0,"schoolId":"288069341826519040","userName":"章银平"}]
     * lookNum : 74
     * newsId : 387751390883090432
     * praiseNum : 0
     * replyNum : 5
     * status : 3
     * summary : 这是小明第四次请小红吃饭！
     * title : 小明第一次请小红去吃大餐
     */

    private String categoryId;
    private String content;
    private String createTime;
    private String createUser;
    private String headImg;
    private int lookNum;
    private String newsId;
    private int praiseNum;
    private int replyNum;
    private String status;
    private String summary;
    private String title;
    private List<CompanyNewsCommentDetail.ReplyListBean> list;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CompanyNewsCommentDetail.ReplyListBean> getList() {
        return list;
    }

    public void setList(List<CompanyNewsCommentDetail.ReplyListBean> list) {
        this.list = list;
    }

    /*public static class ListBean implements Serializable {
        *//**
         * createTime : 2017-06-05 11:42:17
         * createUser : 288078455667429377
         * deleteFlag : 1
         * newsId : 387751390883090432
         * photoUrl : http://file.1000xyun.com:81/group1/M00/00/09/wKgDZ1kTyTuAZLrSAABUNV_Omaw112.jpg?attname=0.jpg
         * praiseNum : 0
         * praiseStatus : 0
         * replyContent : 哈哈，白痴小明3
         * replyId : 389146027094970368
         * replyNum : 0
         * replyType : 0
         * schoolId : 288069341826519040
         * userName : 章银平
         *//*

        private String createTime;
        private String createUser;
        private int deleteFlag;
        private String newsId;
        private String photoUrl;
        private int praiseNum;
        private int praiseStatus;
        private String replyContent;
        private String replyId;
        private int replyNum;
        private int replyType;
        private String schoolId;
        private String userName;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public int getDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(int deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getNewsId() {
            return newsId;
        }

        public void setNewsId(String newsId) {
            this.newsId = newsId;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public int getPraiseNum() {
            return praiseNum;
        }

        public void setPraiseNum(int praiseNum) {
            this.praiseNum = praiseNum;
        }

        public int getPraiseStatus() {
            return praiseStatus;
        }

        public void setPraiseStatus(int praiseStatus) {
            this.praiseStatus = praiseStatus;
        }

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public int getReplyNum() {
            return replyNum;
        }

        public void setReplyNum(int replyNum) {
            this.replyNum = replyNum;
        }

        public int getReplyType() {
            return replyType;
        }

        public void setReplyType(int replyType) {
            this.replyType = replyType;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }*/
}
