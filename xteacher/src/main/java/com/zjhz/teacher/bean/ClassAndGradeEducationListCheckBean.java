package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ClassAndGradeEducationListCheckBean implements Serializable {
    public String name = "";     // 班级
    public String classId = "";  // 班级ID
    public String moralName = ""; // 项目
    public String moralId = ""; // 项目Id
    public int scoreMode = 0;  // scoreMode:得分方式：0减分1加分
    public int meterMode = 0;  // meterMode:计分方式：0固定分值1评分制
    public int meterUnit = 0;  // meterUnit:计分单位 也就是多少分
    public String meterRange = "";
    public String inspector = "";//督导员id
    public String score = "0";
    public String min = "";
    public String max = "";
    public boolean isChange = false;
}
