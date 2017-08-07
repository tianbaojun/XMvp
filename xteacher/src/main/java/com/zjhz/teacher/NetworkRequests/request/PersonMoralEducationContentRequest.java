package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-02
 * Time: 14:23
 * Description: 个人德育   个人德育录入 提交
 */
public class PersonMoralEducationContentRequest implements Serializable {

    public String checkTime; // 检查时间
    public String moralId;   // 所选项目ID
    public String inspector; // 督导员ID
    public String targetor;  // 被检查学生id
    public String score;     // 所选项目所对应的分数
    public String moralManId;//个人检查id
    /**
     * @param checkTime 检查时间
     * @param moralId   所选项目ID
     * @param inspector 督导员ID
     * @param targetor  被检查学生id
     * @param score     所选项目所对应的分数
     */
    public PersonMoralEducationContentRequest(String checkTime, String moralId, String inspector, String targetor, String score) {
        this.checkTime = checkTime;
        this.moralId = moralId;
        this.inspector = inspector;
        this.targetor = targetor;
        this.score = score;
    }

    /**
     * @param moralManId    所选项目ID
     * @param inspector 督导员ID
     * @param score     所选项目所对应的分数
     */
    public PersonMoralEducationContentRequest(String moralManId , String inspector, String score) {
        this.moralManId  = moralManId ;
        this.inspector = inspector;
        this.score = score;
    }
}
