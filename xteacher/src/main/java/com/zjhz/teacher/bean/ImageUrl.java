package com.zjhz.teacher.bean;

import java.io.Serializable;

public class ImageUrl implements Serializable {

        private String picPath,createTime,schoolId,picSort,createUser,dId,picId;

        public String getPicPath() {
            return picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getPicSort() {
            return picSort;
        }

        public void setPicSort(String picSort) {
            this.picSort = picSort;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getdId() {
            return dId;
        }

        public void setdId(String dId) {
            this.dId = dId;
        }

        public String getPicId() {
            return picId;
        }

        public void setPicId(String picId) {
            this.picId = picId;
        }
    }