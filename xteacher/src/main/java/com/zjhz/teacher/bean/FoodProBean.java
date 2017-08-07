package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/6.
 * Description:
 * What Changed:
 */

public class FoodProBean implements Serializable {

    /**
     * content : 呢
     * cookbookId : 387375878046224384
     * createTime : 2017-05-31 14:28:21
     * createUser : 288078455709372424
     * currentUserPraiseStatus : 2
     * imageUrls : [{"cookbookId":"387375878046224384","createTime":"2017-05-31 00:00:00","createUser":"288078455709372424","docName":"wKgDZ1kuYoWAbD1vAAdiJYYG_Mo608.jpg?attname=IMG_20170516_150854.jpg","docPath":"http://file.1000xyun.com:81/group1/M00/00/0D/wKgDZ1kuYoWAbD1vAAdiJYYG_Mo608.jpg?attname=IMG_20170516_150854.jpg","imgId":"387375878100750336"}]
     * listComment : [{"brepUserName":"","createTime":"2017-05-31 14:35:55","createUser":"288078455713566737","msgContect":"去温泉翁","msgId":"387375878046224384","msgOrder":387375878046224400,"msgType":2,"replyId":"387377784361586688","replyTime":"2017-05-31 14:35:55","replyUser":"288078455713566737","schoolId":"288069341826519040","userName":"王佳丽"}]
     * listPraise : [{"createTime":"2017-05-11 09:48:36","createUser":"288114335132160015","msgId":"368194874316427264","praiseId":"380057719991701504","praiseNum":1,"praiseTime":"2017-05-11 09:48:36","praiseType":2,"praiseUser":"288114335132160015","schoolId":"288069341826519040","userName":"宋励"}]
     * lookNum : 22
     * name : 黄媛媛
     * pattern : 2
     * photoUrl : http://file.1000xyun.com:81/group1/M00/00/0D/wKgDZ1kyM9WAb90XAABam0Nuwl4412.jpg?attname=headerImg.jpg
     * praiseNum : 0
     * publishPerson : 288078455709372400
     * publishTime : 2017-05-31 14:28:21
     * replyNum : 1
     * schoolId : 288069341826519040
     * status : 1
     */

    private String content;
    private String cookbookId;
    private String createTime;
    private String createUser;
    private String currentUserPraiseStatus;
    private int lookNum;
    private String name;
    private String pattern;
    private String photoUrl;
    private int praiseNum;
    private long publishPerson;
    private String publishTime;
    private int replyNum;
    private String schoolId;
    private String status;
    private List<ImageUrlsBean> imageUrls;
    private List<ListCommentBean> listComment;
    private List<ListPraiseBean> listPraise;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCookbookId() {
        return cookbookId;
    }

    public void setCookbookId(String cookbookId) {
        this.cookbookId = cookbookId;
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

    public String getCurrentUserPraiseStatus() {
        return currentUserPraiseStatus;
    }

    public void setCurrentUserPraiseStatus(String currentUserPraiseStatus) {
        this.currentUserPraiseStatus = currentUserPraiseStatus;
    }

    public int getLookNum() {
        return lookNum;
    }

    public void setLookNum(int lookNum) {
        this.lookNum = lookNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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

    public long getPublishPerson() {
        return publishPerson;
    }

    public void setPublishPerson(long publishPerson) {
        this.publishPerson = publishPerson;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ImageUrlsBean> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageUrlsBean> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<ListCommentBean> getListComment() {
        return listComment;
    }

    public void setListComment(List<ListCommentBean> listComment) {
        this.listComment = listComment;
    }

    public List<ListPraiseBean> getListPraise() {
        return listPraise;
    }

    public void setListPraise(List<ListPraiseBean> listPraise) {
        this.listPraise = listPraise;
    }

    public static class ImageUrlsBean implements Serializable{
        /**
         * cookbookId : 387375878046224384
         * createTime : 2017-05-31 00:00:00
         * createUser : 288078455709372424
         * docName : wKgDZ1kuYoWAbD1vAAdiJYYG_Mo608.jpg?attname=IMG_20170516_150854.jpg
         * docPath : http://file.1000xyun.com:81/group1/M00/00/0D/wKgDZ1kuYoWAbD1vAAdiJYYG_Mo608.jpg?attname=IMG_20170516_150854.jpg
         * imgId : 387375878100750336
         */

        private String cookbookId;
        private String createTime;
        private String createUser;
        private String docName;
        private String docPath;
        private String imgId;

        public String getCookbookId() {
            return cookbookId;
        }

        public void setCookbookId(String cookbookId) {
            this.cookbookId = cookbookId;
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

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getDocPath() {
            return docPath;
        }

        public void setDocPath(String docPath) {
            this.docPath = docPath;
        }

        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }
    }

    public static class ListCommentBean implements Serializable {
        /**
         * brepUserName :
         * createTime : 2017-05-31 14:35:55
         * createUser : 288078455713566737
         * msgContect : 去温泉翁
         * msgId : 387375878046224384
         * msgOrder : 387375878046224400
         * msgType : 2
         * replyId : 387377784361586688
         * replyTime : 2017-05-31 14:35:55
         * replyUser : 288078455713566737
         * schoolId : 288069341826519040
         * userName : 王佳丽
         */

        private String brepUserName;
        private String createTime;
        private String createUser;
        private String msgContect;
        private String msgId;
        private long msgOrder;
        private int msgType;
        private String replyId;
        private String replyTime;
        private String replyUser;
        private String schoolId;
        private String userName;

        public String getBrepUserName() {
            return brepUserName;
        }

        public void setBrepUserName(String brepUserName) {
            this.brepUserName = brepUserName;
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

        public String getMsgContect() {
            return msgContect;
        }

        public void setMsgContect(String msgContect) {
            this.msgContect = msgContect;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public long getMsgOrder() {
            return msgOrder;
        }

        public void setMsgOrder(long msgOrder) {
            this.msgOrder = msgOrder;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public String getReplyTime() {
            return replyTime;
        }

        public void setReplyTime(String replyTime) {
            this.replyTime = replyTime;
        }

        public String getReplyUser() {
            return replyUser;
        }

        public void setReplyUser(String replyUser) {
            this.replyUser = replyUser;
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

    public static class ListPraiseBean implements Serializable{
        /**
         * createTime : 2017-05-11 09:48:36
         * createUser : 288114335132160015
         * msgId : 368194874316427264
         * praiseId : 380057719991701504
         * praiseNum : 1
         * praiseTime : 2017-05-11 09:48:36
         * praiseType : 2
         * praiseUser : 288114335132160015
         * schoolId : 288069341826519040
         * userName : 宋励
         */

        private String createTime;
        private String createUser;
        private String msgId;
        private String praiseId;
        private int praiseNum;
        private String praiseTime;
        private int praiseType;
        private String praiseUser;
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

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getPraiseId() {
            return praiseId;
        }

        public void setPraiseId(String praiseId) {
            this.praiseId = praiseId;
        }

        public int getPraiseNum() {
            return praiseNum;
        }

        public void setPraiseNum(int praiseNum) {
            this.praiseNum = praiseNum;
        }

        public String getPraiseTime() {
            return praiseTime;
        }

        public void setPraiseTime(String praiseTime) {
            this.praiseTime = praiseTime;
        }

        public int getPraiseType() {
            return praiseType;
        }

        public void setPraiseType(int praiseType) {
            this.praiseType = praiseType;
        }

        public String getPraiseUser() {
            return praiseUser;
        }

        public void setPraiseUser(String praiseUser) {
            this.praiseUser = praiseUser;
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
