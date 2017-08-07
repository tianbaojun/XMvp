package com.zjhz.teacher.NetworkRequests.response;

import com.zjhz.teacher.bean.StudentBean;

import java.io.Serializable;
import java.util.List;

/**
 * 班级名称ID,
 * Created by Administrator on 2016/7/16.
 */
public class ClassesBeans implements Serializable {
    private String gradeId = "";
    private String classId = "";
    private String name= "";
    private String className = "";
    private String homeworkId= "";
    private String linkId= "";
    private String schoolId= "";
    private String studentNum;
    private String studentNumString;
    private boolean isChecd = false;
    /**
     * stuNum : 55
     * subNum : 0
     */

    private String stuNum;
    private String subNum;

    private List<StudentBean> studentList;


    public boolean isChecd() {
        return isChecd;
    }



    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setChecd(boolean checd) {
        isChecd = checd;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getSubNum() {
        return subNum;
    }

    public void setSubNum(String subNum) {
        this.subNum = subNum;
    }

    public List<StudentBean> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentBean> studentList) {
        this.studentList = studentList;
    }

    public String getStudentNumString() {
        studentNumString = "共： "+studentNum+"  人";
        return studentNumString;
    }

    public void setStudentNumString(String studentNumString) {
        this.studentNumString = studentNumString;
    }
}