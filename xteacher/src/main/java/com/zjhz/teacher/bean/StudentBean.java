package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by zzd on 2017/6/19.
 */

public class StudentBean implements Serializable{


    private String name;
    private String photoUrl;
    /**
     * studentId : 288114322884792320
     * gradeName : 一年级
     * classId : 288113366604451840
     * gradeId : 288073659753435136
     * idCard : 201500000000000004
     * className : 一（1）班
     */

    private String studentId;
    private String gradeName;
    private String classId;
    private String gradeId;
    private String idCard;
    private String className;
    private int flag = -1;
    private String certificateId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }
}
