package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description: 学生当前位置年级班级查询查询
 */
public class StudentsCurrentPositionClassGrade implements Serializable {

    public String gradeName;
    public String gradeId;
    public List<StudentsCurrentPositionClass> clazzs = new ArrayList<>();
}
