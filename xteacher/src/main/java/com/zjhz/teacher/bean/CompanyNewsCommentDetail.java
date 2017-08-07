package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class CompanyNewsCommentDetail implements Serializable {

    /**
     * createTime : 2017-06-05 10:19:08
     * createUser : 288078455667429377
     * deleteFlag : 0
     * newsId : 388023872244420608
     * photoUrl : http://file.1000xyun.com:81/group1/M00/00/09/wKgDZ1kTyTuAZLrSAABUNV_Omaw112.jpg?attname=0.jpg
     * praiseNum : 0
     * praiseStatus : 0
     * replyContent : 哈哈，白痴小明
     * replyId : 389125099782934528
     * replyList : [{"createTime":"2017-06-09 13:54:23","createUser":"288078455667429377","deleteFlag":1,"newsId":"388023872244420608","photoUrl":"http://file.1000xyun.com:81/group1/M00/00/09/wKgDZ1kTyTuAZLrSAABUNV_Omaw112.jpg?attname=0.jpg","praiseNum":0,"praiseStatus":0,"refReplyId":"389125099782934528","replyContent":"你同事呢223？","replyId":"390628821243531264","replyNum":0,"replyType":1,"replyUser":"288078455667429377","replyUserName":"章银平","schoolId":"288069341826519040","userName":"章银平"},{"createTime":"2017-06-09 13:54:01","createUser":"288078455667429377","deleteFlag":1,"newsId":"388023872244420608","photoUrl":"http://file.1000xyun.com:81/group1/M00/00/09/wKgDZ1kTyTuAZLrSAABUNV_Omaw112.jpg?attname=0.jpg","praiseNum":0,"praiseStatus":0,"refReplyId":"389125099782934528","replyContent":"你同事呢22？","replyId":"390628729396662272","replyNum":0,"replyType":1,"replyUser":"288078455667429377","replyUserName":"章银平","schoolId":"288069341826519040","userName":"章银平"}]
     * replyNum : 2
     * replyType : 0
     * schoolId : 288069341826519040
     * userName : 章银平
     */

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
    private List<ReplyListBean> replyList;

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

    public List<ReplyListBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ReplyListBean> replyList) {
        this.replyList = replyList;
    }

    public static class ReplyListBean implements Serializable{
        /**
         * createTime : 2017-06-09 13:54:23
         * createUser : 288078455667429377
         * deleteFlag : 1
         * newsId : 388023872244420608
         * photoUrl : http://file.1000xyun.com:81/group1/M00/00/09/wKgDZ1kTyTuAZLrSAABUNV_Omaw112.jpg?attname=0.jpg
         * praiseNum : 0
         * praiseStatus : 0
         * refReplyId : 389125099782934528
         * replyContent : 你同事呢223？
         * replyId : 390628821243531264
         * replyNum : 0
         * replyType : 1
         * replyUser : 288078455667429377
         * replyUserName : 章银平
         * schoolId : 288069341826519040
         * userName : 章银平
         */

        private String createTime;
        private String rootId;
        private String createUser;
        private int deleteFlag;
        private String newsId;
        private String photoUrl;
        private int praiseNum;
        private int praiseStatus;
        private String refReplyId;
        private String replyContent;
        private String replyId;
        private int replyNum;
        private int replyType;
        private String replyUser;
        private String replyUserName;
        private String schoolId;
        private String userName;

        public String getRootId() {
            return rootId;
        }

        public void setRootId(String rootId) {
            this.rootId = rootId;
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

        public String getRefReplyId() {
            return refReplyId;
        }

        public void setRefReplyId(String refReplyId) {
            this.refReplyId = refReplyId;
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

        public String getReplyUser() {
            return replyUser;
        }

        public void setReplyUser(String replyUser) {
            this.replyUser = replyUser;
        }

        public String getReplyUserName() {
            return replyUserName;
        }

        public void setReplyUserName(String replyUserName) {
            this.replyUserName = replyUserName;
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
    }
}
