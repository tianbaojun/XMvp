package com.zjhz.teacher.NetworkRequests.retrofit.Model;


import com.zjhz.teacher.NetworkRequests.response.UserLogin;
import com.zjhz.teacher.bean.BaseModel;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class LoginRespon extends BaseModel {


    /**
     * msg : 登录成功！
     * code : 0
     * total : 1
     * data : {"imMark":"1","os":"APP","sources":[{"sourceType":"2"}],"TimeOut":14400,"nickName":"章银平","roles":[],"userId":"288078455667429377","phoneNo":"14957576283","token":"4df3f3df98ce815894430806d852dd72","photoUrl":"","loginTime":1502870176221,"teacherId":"288078455667429376","sourceType":"2","schoolId":"288069341826519040","schoolName":"杭州智鹏千校云","jobNumber":"18001"}
     */

    private String msg;
    private int code;
    private int total;
    private UserLogin data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
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

    public UserLogin getData() {
        return data;
    }

    public void setData(UserLogin data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * imMark : 1
         * os : APP
         * sources : [{"sourceType":"2"}]
         * TimeOut : 14400
         * nickName : 章银平
         * roles : []
         * userId : 288078455667429377
         * phoneNo : 14957576283
         * token : 4df3f3df98ce815894430806d852dd72
         * photoUrl :
         * loginTime : 1502870176221
         * teacherId : 288078455667429376
         * sourceType : 2
         * schoolId : 288069341826519040
         * schoolName : 杭州智鹏千校云
         * jobNumber : 18001
         */

        private String imMark;
        private String os;
        private int TimeOut;
        private String nickName;
        private String userId;
        private String phoneNo;
        private String token;
        private String photoUrl;
        private long loginTime;
        private String teacherId;
        private String sourceType;
        private String schoolId;
        private String schoolName;
        private String jobNumber;
        private List<SourcesBean> sources;
        private List<?> roles;

        public String getImMark() {
            return imMark;
        }

        public void setImMark(String imMark) {
            this.imMark = imMark;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public int getTimeOut() {
            return TimeOut;
        }

        public void setTimeOut(int TimeOut) {
            this.TimeOut = TimeOut;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(long loginTime) {
            this.loginTime = loginTime;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
        }

        public List<SourcesBean> getSources() {
            return sources;
        }

        public void setSources(List<SourcesBean> sources) {
            this.sources = sources;
        }

        public List<?> getRoles() {
            return roles;
        }

        public void setRoles(List<?> roles) {
            this.roles = roles;
        }

        public static class SourcesBean {
            /**
             * sourceType : 2
             */

            private String sourceType;

            public String getSourceType() {
                return sourceType;
            }

            public void setSourceType(String sourceType) {
                this.sourceType = sourceType;
            }
        }
    }
}
