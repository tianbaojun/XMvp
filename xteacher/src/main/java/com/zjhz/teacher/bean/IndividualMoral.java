package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/8.
 */
public class IndividualMoral implements Serializable{

    private String classId, moralId,moralName;
    private double score;

    public String getScore() {
        return String.valueOf(Math.round(score * 10) / 10.0);
    }

    public String getMoralName() {
        return moralName;
    }
}
