package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24.
 */

public class EduCount implements Serializable {
    private String className ,classId ,classMoralScore,individualMoralScore,totalScore;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassMoralScore() {
        return classMoralScore;
    }

    public void setClassMoralScore(String classMoralScore) {
        this.classMoralScore = classMoralScore;
    }

    public String getIndividualMoralScore() {
        return individualMoralScore;
    }

    public void setIndividualMoralScore(String individualMoralScore) {
        this.individualMoralScore = individualMoralScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }
}
