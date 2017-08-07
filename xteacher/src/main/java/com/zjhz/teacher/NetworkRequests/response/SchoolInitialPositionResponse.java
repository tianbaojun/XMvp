package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-7
 * Time: 13:17
 * Description: 校外定位 学校初始位置响应数据
 */
public class SchoolInitialPositionResponse implements Serializable {
    /**
     * msg : null
     * code : 0
     * total : 1
     * data : {"summary":"","address":"北京市朝阳区368","latitude":"","schoolType":"","updateUser":"272519939070365696","updateTime":"2016-08-15 10:07:03","createTime":"2016-08-12 14:19:14","domain":"","imageUrl":"","schoolId":"281556330072772609","createUser":"272519939070365696","region":"吉林省/","schoolName":"朝阳小学","ext1":"","longitude":"","schoolCode":"cyxx","status":"1"}
     */

    private Object msg;
    private int code;
    private int total;
    /**
     * summary :
     * address : 北京市朝阳区368
     * latitude :
     * schoolType :
     * updateUser : 272519939070365696
     * updateTime : 2016-08-15 10:07:03
     * createTime : 2016-08-12 14:19:14
     * domain :
     * imageUrl :
     * schoolId : 281556330072772609
     * createUser : 272519939070365696
     * region : 吉林省/
     * schoolName : 朝阳小学
     * ext1 :
     * longitude :
     * schoolCode : cyxx
     * status : 1
     */

    private DataBean data;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private String summary;
        private String address;
        private String latitude;
        private String schoolType;
        private String updateUser;
        private String updateTime;
        private String createTime;
        private String domain;
        private String imageUrl;
        private String schoolId;
        private String createUser;
        private String region;
        private String schoolName;
        private String ext1;
        private String longitude;
        private String schoolCode;
        private String status;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getSchoolType() {
            return schoolType;
        }

        public void setSchoolType(String schoolType) {
            this.schoolType = schoolType;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getExt1() {
            return ext1;
        }

        public void setExt1(String ext1) {
            this.ext1 = ext1;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getSchoolCode() {
            return schoolCode;
        }

        public void setSchoolCode(String schoolCode) {
            this.schoolCode = schoolCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

//
//    /**
//     * code : 0
//     * data : {"address":"浙江省绍兴市环城西路501号","admin":{},"createTime":"2016-07-16 00:00:00","createUser":2,"domain":"http://www.luxiao.com/","latitude":"30.072411","longitude":"120.649629","nature":"1","region":"浙江省/绍兴市/越城区/","scale":"2","schoolCode":"mszx","schoolId":"271688204724211712","schoolName":"绍兴市鲁迅小学","schoolType":"3","status":"1","summary":"测试学校","updateTime":"2016-07-23 10:02:45","updateUser":272484438246428672}
//     * total : 1
//     */
//
//    public int code;
//    /**
//     * address : 浙江省绍兴市环城西路501号
//     * admin : {}
//     * createTime : 2016-07-16 00:00:00
//     * createUser : 2
//     * domain : http://www.luxiao.com/
//     * latitude : 30.072411
//     * longitude : 120.649629
//     * nature : 1
//     * region : 浙江省/绍兴市/越城区/
//     * scale : 2
//     * schoolCode : mszx
//     * schoolId : 271688204724211712
//     * schoolName : 绍兴市鲁迅小学
//     * schoolType : 3
//     * status : 1
//     * summary : 测试学校
//     * updateTime : 2016-07-23 10:02:45
//     * updateUser : 272484438246428672
//     */
//
//    public DataBean data;
//    public int total;
//
//    public static class DataBean  implements Serializable {
//        public String address;
//        public String createTime;
//        public int createUser;
//        public String domain;
//        public String latitude;
//        public String longitude;
//        public String nature;
//        public String region;
//        public String scale;
//        public String schoolCode;
//        public String schoolId;
//        public String schoolName;
//        public String schoolType;
//        public String status;
//        public String summary;
//        public String updateTime;
//        public long updateUser;
//    }
}
