package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzd on 2017/7/1.
 */

public class HomeworkUploadDetailBean implements Serializable {

    /**
     * content : 我错了，我不应该说（我的作业不是很想做啊，老师会不会骂我）
     * editFlag : 1
     * imgList : [{"createTime":"2017-06-22 00:00:00","createUser":"272484438288371712","docName":"test1.png","docPath":"d:/test1.png","imgId":"395339064074571776","schoolId":"288069341826519040","taskId":"395298674474749952"},{"createTime":"2017-06-22 00:00:00","createUser":"272484438288371712","docName":"test2.png","docPath":"d:/test2.png","imgId":"395339064074571777","schoolId":"288069341826519040","taskId":"395298674474749952"},{"createTime":"2017-06-22 00:00:00","createUser":"272484438288371712","docName":"test3.png","docPath":"d:/test3.png","imgId":"395339064074571778","schoolId":"288069341826519040","taskId":"395298674474749952"}]
     * praiseLevel : SYS_PRAISE_LEVEL_1
     * praiseLevelName : 优
     * praiseTime : 2017-06-23 14:30:47
     * praiseUser : 288078455738732560
     * praiseUserName : 王旺
     * taskId : 395298674474749952
     */

    private String content;
    private int editFlag;
    private String praiseLevel;
    private String praiseLevelName;
    private String createTime;
    private String praiseUser;
    private String praiseUserName;
    private String taskId;

    private String praiseContent;

    private List<ImgListBean> imgList;
    private List<voiceListBean> voiceList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(int editFlag) {
        this.editFlag = editFlag;
    }

    public String getPraiseLevel() {
        switch (getPraiseLevelName()){
            case "优":
                praiseLevel = "SYS_PRAISE_LEVEL_1";
                break;
            case "良":
                praiseLevel = "SYS_PRAISE_LEVEL_2";
                break;
            case "中":
                praiseLevel = "SYS_PRAISE_LEVEL_3";
                break;
            case "及格":
                praiseLevel = "SYS_PRAISE_LEVEL_4";
                break;
            case "不及格":
                praiseLevel = "SYS_PRAISE_LEVEL_5";
                break;
        }
        return praiseLevel;
    }

    public void setPraiseLevel(String praiseLevel) {
        this.praiseLevel = praiseLevel;
    }

    public String getPraiseLevelName() {
        return praiseLevelName;
    }

    public void setPraiseLevelName(String praiseLevelName) {
        this.praiseLevelName = praiseLevelName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPraiseUser() {
        return praiseUser;
    }

    public void setPraiseUser(String praiseUser) {
        this.praiseUser = praiseUser;
    }

    public String getPraiseUserName() {
        return praiseUserName;
    }

    public void setPraiseUserName(String praiseUserName) {
        this.praiseUserName = praiseUserName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPraiseContent() {
        return praiseContent;
    }

    public void setPraiseContent(String praiseContent) {
        this.praiseContent = praiseContent;
    }

    public List<ImgListBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgListBean> imgList) {
        this.imgList = imgList;
    }

    public List<voiceListBean> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<voiceListBean> voiceList) {
        this.voiceList = voiceList;
    }

    public static class ImgListBean implements Serializable{
        /**
         * createTime : 2017-06-22 00:00:00
         * createUser : 272484438288371712
         * docName : test1.png
         * docPath : d:/test1.png
         * imgId : 395339064074571776
         * schoolId : 288069341826519040
         * taskId : 395298674474749952
         */

        private String createTime;
        private String createUser;
        private String docName;
        private String docPath;
        private String imgId;
        private String schoolId;
        private String taskId;

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

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
    }
    public static class voiceListBean implements Serializable {

        /**
         * attId : 407309058530349056
         * attPath : http://file.1000xyun.com:81/group1/M00/00/19/wKgDZ1l25r2AXwUgAAAeUjtZqDY395.mp3?attname=39c2b2ea-f106-4b5f-bdf6-8e5c17e5c085.mp3
         * attExtName : mp3
         * createTime : 2017-07-25 14:35:41
         * attSort : 0
         * attName :
         * taskId : 407309058463240192
         */

        private String attId;
        private String attPath;
        private String attExtName;
        private String createTime;
        private int attSort;
        private String attName;
        private String taskId;

        private int remarks;

        public String getAttId() {
            return attId;
        }

        public void setAttId(String attId) {
            this.attId = attId;
        }

        public String getAttPath() {
            return attPath;
        }

        public void setAttPath(String attPath) {
            this.attPath = attPath;
        }

        public String getAttExtName() {
            return attExtName;
        }

        public void setAttExtName(String attExtName) {
            this.attExtName = attExtName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getAttSort() {
            return attSort;
        }

        public void setAttSort(int attSort) {
            this.attSort = attSort;
        }

        public String getAttName() {
            return attName;
        }

        public void setAttName(String attName) {
            this.attName = attName;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getRemarks() {
            return remarks;
        }

        public void setRemarks(int remarks) {
            this.remarks = remarks;
        }
    }
}
