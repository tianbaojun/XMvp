package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 *班级德育删除
 */
public class GrowthAppraiseBean implements Serializable {

    /**
     * appraiseId : 392036602001494016
     * content : 我说：勤奋不是事业成功的忠实伴侣。
     * createTime : 2017-06-13 14:28:12
     * createUser : 288114334976970768
     * defaultId : 392086882730643456
     * schoolId : 288069341826519040
     * updateTime : 2017-06-13 14:31:09
     * updateUser : 288114334976970768
     */

    private String appraiseId;
    private String content;
    private String createTime;
    private String createUser;
    private String defaultId;
    private String schoolId;
    private String updateTime;
    private String updateUser;

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
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

    public String getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(String defaultId) {
        this.defaultId = defaultId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
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
