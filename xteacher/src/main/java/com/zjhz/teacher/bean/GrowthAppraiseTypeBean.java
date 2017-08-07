package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 *班级德育删除
 */
public class GrowthAppraiseTypeBean implements Serializable {

    /**
     * appType : 0
     * appraiseCode : 1001
     * appraiseId : 392036602001494016
     * appraiseName : 学期寄语
     * createTime : 2017-06-13 11:08:24
     * createUser : 288114334976970768
     * describes : 描学期寄语-述
     * image : 0
     * rowSort : 1
     * schoolId : 288069341826519040
     * startStatus : 1
     * updateTime : 2017-06-13 13:39:10
     * updateUser : 288114334976970768
     */

    private int appType;
    private int appraiseCode;
    private String appraiseId;
    private String appraiseName;
    private String createTime;
    private String createUser;
    private String describes;
    private String image;
    private String schoolId;
    private int startStatus;
//    private String updateTime;
//    private String updateUser;

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getAppraiseCode() {
        return appraiseCode;
    }

    public void setAppraiseCode(int appraiseCode) {
        this.appraiseCode = appraiseCode;
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }

    public String getAppraiseName() {
        return appraiseName;
    }

    public void setAppraiseName(String appraiseName) {
        this.appraiseName = appraiseName;
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

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(int startStatus) {
        this.startStatus = startStatus;
    }
}
