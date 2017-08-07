package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class SaiXuanbean implements Serializable {

    private List<Type> typeList;
    private List<Prop> propList;

    public SaiXuanbean() {
        typeList = new ArrayList<>();
        propList = new ArrayList<>();
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public List<Prop> getPropList() {
        return propList;
    }

    public class Type implements Serializable{

        /**
         * createTime : 2017-07-06 13:57:20
         * createUser : 288078455709372432
         * optTypeId : 400414036816039936
         * schoolId : 288069341826519040
         * status : 1
         * summary : 体育
         * typeName : 体育类
         * updateTime : 2017-07-10 10:26:48
         * updateUser : 288078455713566737
         */

        private String createTime;
        private String createUser;
        private String optTypeId;
        private String schoolId;
        private String status;
        private String summary;
        private String typeName;
        private String updateTime;
        private String updateUser;

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

        public String getOptTypeId() {
            return optTypeId;
        }

        public void setOptTypeId(String optTypeId) {
            this.optTypeId = optTypeId;
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

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }
    }

    public class Prop implements Serializable{

        /**
         * createTime : 2017-07-06 13:58:13
         * createUser : 288078455709372432
         * optPropId : 400414257906192384
         * propName : 基础课程
         * schoolId : 288069341826519040
         * status : 1
         * updateTime : 2017-07-06 13:58:13
         * updateUser : 288078455709372432
         */

        private String createTime;
        private String createUser;
        private String optPropId;
        private String propName;
        private String schoolId;
        private String status;
        private String updateTime;
        private String updateUser;

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

        public String getOptPropId() {
            return optPropId;
        }

        public void setOptPropId(String optPropId) {
            this.optPropId = optPropId;
        }

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
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

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }
    }

}

