package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 *班级德育删除
 */
public class AddBean implements Serializable {
    public String classId; // 班级ID
    public String schemeId; // 德育方案ID
    public String checkTime; // 检查时间
    public String moralIdsWithScore; // 是项目带分数  "283718099654414336@@1
    public String inspector; // 督察员id
    public String score; // 分数
    public String totalScore; // 班级总分


//    public AddBean(String classId, String schemeId, String checkTime, String moralIdsWithScore, String inspector, String totalScore) {
//        this.classId = classId;
//        this.schemeId = schemeId;
//        this.checkTime = checkTime;
//        this.moralIdsWithScore = moralIdsWithScore;
//        this.inspector = inspector;
//        this.totalScore = totalScore;
//    }
}
