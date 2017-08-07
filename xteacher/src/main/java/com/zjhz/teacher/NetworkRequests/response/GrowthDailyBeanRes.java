package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzd on 2017/6/20.
 */

public class GrowthDailyBeanRes implements Serializable {

    /**
     * categoryTitle : 我的成长日记
     * content : 成长记事我是测试,我是第二次
     * createTime : 2017-06-26 09:23:26
     * createUser : 288078455692595202
     * detailList : [{"createTime":"2017-06-26 09:23:26","createUser":"288078455692595202","memoryId":"396721229706301440","path":"pic16.png","picId":"396721229706301441","schoolId":"288069341826519040","sort":0},{"createTime":"2017-06-26 09:23:26","createUser":"288078455692595202","memoryId":"396721229706301440","path":"pic27.png","picId":"396721229706301442","schoolId":"288069341826519040","sort":1},{"createTime":"2017-06-26 09:23:26","createUser":"288078455692595202","memoryId":"396721229706301440","path":"pic38.png","picId":"396721229706301443","schoolId":"288069341826519040","sort":2}]
     * memoryId : 396721229706301440
     * schoolId : 288069341826519040
     * shareFlag : 0
     * studentId : 288114323459411970
     */

    private String date;
    private String categoryTitle;
    private String content;
    private String createTime;
    private String createUser;
    private String memoryId;
    private String schoolId;
    private int shareFlag;
    private String studentId;
    private List<DetailListBean> detailList;
//    private List<ListAttachmentBean> listAttachment;

    public String getDate() {
        if(createTime == null){
            return null;
        }else {
            int month = Integer.parseInt(createTime.substring(5,7));
            int day = Integer.parseInt(createTime.substring(8,10));
            date = month +"月" + day + "日";
        }
        return date;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
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

    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(String memoryId) {
        this.memoryId = memoryId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getShareFlag() {
        return shareFlag;
    }

    public void setShareFlag(int shareFlag) {
        this.shareFlag = shareFlag;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<DetailListBean> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DetailListBean> detailList) {
        this.detailList = detailList;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public List<ListAttachmentBean> getListAttachment() {
//        return listAttachment;
//    }
//
//    public void setListAttachment(List<ListAttachmentBean> listAttachment) {
//        this.listAttachment = listAttachment;
//    }

    public static class DetailListBean implements Serializable{
        /**
         * createTime : 2017-06-26 09:23:26
         * createUser : 288078455692595202
         * memoryId : 396721229706301440
         * path : pic16.png
         * picId : 396721229706301441
         * schoolId : 288069341826519040
         * sort : 0
         */


        private String createTime;
        private String createUser;
        private String memoryId;
        private String path;
        private String picId;
        private String schoolId;
        private int sort;
        private String picPath;
        private String remarks;
        private String attExtName;
        /**
         * memoryId : 395298674474749950
         * name : test2.png
         */
        private String name;

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

        public String getMemoryId() {
            return memoryId;
        }

        public void setMemoryId(String memoryId) {
            this.memoryId = memoryId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPicId() {
            return picId;
        }

        public void setPicId(String picId) {
            this.picId = picId;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicPath() {
            return picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getAttExtName() {
            return attExtName;
        }

        public void setAttExtName(String attExtName) {
            this.attExtName = attExtName;
        }
    }

//    public static class ListAttachmentBean implements Serializable{
//
//        /**
//         * picPath : http://file.1000xyun.com:81/group1/M00/00/26/wKgDZ1l5XzaALnOhAAQsZSHYDhA513.jpg?attname=18fcbb0b-1822-49bf-b185-962688328ac8.jpg
//         * attId : 407988174107709441
//         * path : http://file.1000xyun.com:81/group1/M00/00/26/wKgDZ1l5XzeAcIwCABj-OtdqY_M715.mp4?attname=18fcbb0b-1822-49bf-b185-962688328ac8.mp4
//         * attExtName : mp4
//         * schoolId : 288069341826519040
//         * memoryId : 407988174107709440
//         * picId : 407988174170624000
//         * remarks :
//         */
//
//        private String picPath;
//        private String attId;
//        private String path;
//        private String attExtName;
//        private String schoolId;
//        private String memoryId;
//        private String picId;
//        private String remarks;
//
//        public String getPicPath() {
//            return picPath;
//        }
//
//        public void setPicPath(String picPath) {
//            this.picPath = picPath;
//        }
//
//        public String getAttId() {
//            return attId;
//        }
//
//        public void setAttId(String attId) {
//            this.attId = attId;
//        }
//
//        public String getPath() {
//            return path;
//        }
//
//        public void setPath(String path) {
//            this.path = path;
//        }
//
//        public String getAttExtName() {
//            return attExtName;
//        }
//
//        public void setAttExtName(String attExtName) {
//            this.attExtName = attExtName;
//        }
//
//        public String getSchoolId() {
//            return schoolId;
//        }
//
//        public void setSchoolId(String schoolId) {
//            this.schoolId = schoolId;
//        }
//
//        public String getMemoryId() {
//            return memoryId;
//        }
//
//        public void setMemoryId(String memoryId) {
//            this.memoryId = memoryId;
//        }
//
//        public String getPicId() {
//            return picId;
//        }
//
//        public void setPicId(String picId) {
//            this.picId = picId;
//        }
//
//        public String getRemarks() {
//            return remarks;
//        }
//
//        public void setRemarks(String remarks) {
//            this.remarks = remarks;
//        }
//    }



//    /**
//     * ccdContent : 帅不？
//     * classId : 288113367174877184
//     * createTime : 2017-06-03 13:21:01
//     * currentUserPraiseStatus : 2
//     * dId : 388446097300787200
//     * dcCode : 1001
//     * dcId : 373718576370028544
//     * describes : 班级动态
//     * imageUrls : [{"createTime":"2017-06-03 13:21:01","createUser":"288078455709372432","dId":"388446097300787200","picId":"388446097313370112","picPath":"http://file.1000xyun.com:81/group1/M00/00/0D/wKgDZ1kyRz2AORPnAADLH7cJevs746.jpg?attname=pic_uc_1494388652177.jpg","picSort":0,"schoolId":"288069341826519040"}]
//     * keywordsValue :
//     * listPraise : [{"dId":"388446097300787200","dpId":"388446889269268480","dpStatus":"1","praiseTime":"2017-06-03 13:24:10","roleType":"0","schoolId":"288069341826519040","userId":"288078455709372432","userName":"陈诗瑶"}]
//     * photoUrl :
//     * publishName : 陈诗瑶
//     * publishTime : 2017-06-03 13:21:01
//     * publishUser : 288078455709372432
//     * schoolId : 288069341826519040
//     */
//
//    private String ccdContent;
//    private String classId;
//    private String createTime;
//    private String currentUserPraiseStatus;
//    private String dId;
//    private String dcCode;
//    private String dcId;
//    private String describes;
//    private String keywordsValue;
//    private String photoUrl;
//    private String publishName;
//    private String publishTime;
//    private String publishUser;
//    private String schoolId;
//    private List<ImageUrlsBean> imageUrls;
//    private List<ListPraiseBean> listPraise;
//
//    public String getCcdContent() {
//        return ccdContent;
//    }
//
//    public void setCcdContent(String ccdContent) {
//        this.ccdContent = ccdContent;
//    }
//
//    public String getClassId() {
//        return classId;
//    }
//
//    public void setClassId(String classId) {
//        this.classId = classId;
//    }
//
//    public String getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getCurrentUserPraiseStatus() {
//        return currentUserPraiseStatus;
//    }
//
//    public void setCurrentUserPraiseStatus(String currentUserPraiseStatus) {
//        this.currentUserPraiseStatus = currentUserPraiseStatus;
//    }
//
//    public String getDId() {
//        return dId;
//    }
//
//    public void setDId(String dId) {
//        this.dId = dId;
//    }
//
//    public String getDcCode() {
//        return dcCode;
//    }
//
//    public void setDcCode(String dcCode) {
//        this.dcCode = dcCode;
//    }
//
//    public String getDcId() {
//        return dcId;
//    }
//
//    public void setDcId(String dcId) {
//        this.dcId = dcId;
//    }
//
//    public String getDescribes() {
//        return describes;
//    }
//
//    public void setDescribes(String describes) {
//        this.describes = describes;
//    }
//
//    public String getKeywordsValue() {
//        return keywordsValue;
//    }
//
//    public void setKeywordsValue(String keywordsValue) {
//        this.keywordsValue = keywordsValue;
//    }
//
//    public String getPhotoUrl() {
//        return photoUrl;
//    }
//
//    public void setPhotoUrl(String photoUrl) {
//        this.photoUrl = photoUrl;
//    }
//
//    public String getPublishName() {
//        return publishName;
//    }
//
//    public void setPublishName(String publishName) {
//        this.publishName = publishName;
//    }
//
//    public String getPublishTime() {
//        return publishTime;
//    }
//
//    public void setPublishTime(String publishTime) {
//        this.publishTime = publishTime;
//    }
//
//    public String getPublishUser() {
//        return publishUser;
//    }
//
//    public void setPublishUser(String publishUser) {
//        this.publishUser = publishUser;
//    }
//
//    public String getSchoolId() {
//        return schoolId;
//    }
//
//    public void setSchoolId(String schoolId) {
//        this.schoolId = schoolId;
//    }
//
//    public List<ImageUrlsBean> getImageUrls() {
//        return imageUrls;
//    }
//
//    public void setImageUrls(List<ImageUrlsBean> imageUrls) {
//        this.imageUrls = imageUrls;
//    }
//
//    public List<ListPraiseBean> getListPraise() {
//        return listPraise;
//    }
//
//    public void setListPraise(List<ListPraiseBean> listPraise) {
//        this.listPraise = listPraise;
//    }
//
//    public static class ImageUrlsBean {
//        /**
//         * createTime : 2017-06-03 13:21:01
//         * createUser : 288078455709372432
//         * dId : 388446097300787200
//         * picId : 388446097313370112
//         * picPath : http://file.1000xyun.com:81/group1/M00/00/0D/wKgDZ1kyRz2AORPnAADLH7cJevs746.jpg?attname=pic_uc_1494388652177.jpg
//         * picSort : 0
//         * schoolId : 288069341826519040
//         */
//
//        private String createTime;
//        private String createUser;
//        private String dId;
//        private String picId;
//        private String picPath;
//        private int picSort;
//        private String schoolId;
//
//        public String getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(String createTime) {
//            this.createTime = createTime;
//        }
//
//        public String getCreateUser() {
//            return createUser;
//        }
//
//        public void setCreateUser(String createUser) {
//            this.createUser = createUser;
//        }
//
//        public String getDId() {
//            return dId;
//        }
//
//        public void setDId(String dId) {
//            this.dId = dId;
//        }
//
//        public String getPicId() {
//            return picId;
//        }
//
//        public void setPicId(String picId) {
//            this.picId = picId;
//        }
//
//        public String getPicPath() {
//            return picPath;
//        }
//
//        public void setPicPath(String picPath) {
//            this.picPath = picPath;
//        }
//
//        public int getPicSort() {
//            return picSort;
//        }
//
//        public void setPicSort(int picSort) {
//            this.picSort = picSort;
//        }
//
//        public String getSchoolId() {
//            return schoolId;
//        }
//
//        public void setSchoolId(String schoolId) {
//            this.schoolId = schoolId;
//        }
//    }
//
//    public static class ListPraiseBean {
//        /**
//         * dId : 388446097300787200
//         * dpId : 388446889269268480
//         * dpStatus : 1
//         * praiseTime : 2017-06-03 13:24:10
//         * roleType : 0
//         * schoolId : 288069341826519040
//         * userId : 288078455709372432
//         * userName : 陈诗瑶
//         */
//
//        private String dId;
//        private String dpId;
//        private String dpStatus;
//        private String praiseTime;
//        private String roleType;
//        private String schoolId;
//        private String userId;
//        private String userName;
//
//        public String getDId() {
//            return dId;
//        }
//
//        public void setDId(String dId) {
//            this.dId = dId;
//        }
//
//        public String getDpId() {
//            return dpId;
//        }
//
//        public void setDpId(String dpId) {
//            this.dpId = dpId;
//        }
//
//        public String getDpStatus() {
//            return dpStatus;
//        }
//
//        public void setDpStatus(String dpStatus) {
//            this.dpStatus = dpStatus;
//        }
//
//        public String getPraiseTime() {
//            return praiseTime;
//        }
//
//        public void setPraiseTime(String praiseTime) {
//            this.praiseTime = praiseTime;
//        }
//
//        public String getRoleType() {
//            return roleType;
//        }
//
//        public void setRoleType(String roleType) {
//            this.roleType = roleType;
//        }
//
//        public String getSchoolId() {
//            return schoolId;
//        }
//
//        public void setSchoolId(String schoolId) {
//            this.schoolId = schoolId;
//        }
//
//        public String getUserId() {
//            return userId;
//        }
//
//        public void setUserId(String userId) {
//            this.userId = userId;
//        }
//
//        public String getUserName() {
//            return userName;
//        }
//
//        public void setUserName(String userName) {
//            this.userName = userName;
//        }
//    }
}
