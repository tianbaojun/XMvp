package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/4/8.
 */
public class ClassMoral implements Serializable{
    private String schemeName,className,schemeId;

    private double totalScore;

    public String getTotalScore() {
        DecimalFormat df = new DecimalFormat("###.0");
        return String.valueOf(Math.round(totalScore * 10) / 10.0);
    }

    public String getSchemeName() {
        return schemeName;
    }
}
