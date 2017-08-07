package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-02
 * Time: 14:23
 * Description: 个人德育   学生学号获取回来的数据
 */
public class PersonMoralEducationStudentNumberBean implements Serializable{
    public String moralName;  // 扣分项
    public String photoUrl;  // 被扣分学生头像
    public String studentName;  // 被扣分学生名字
    public String score;  // 被扣分学生的分数
    public String id;  // 被扣分学生的id
}
