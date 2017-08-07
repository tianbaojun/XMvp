package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class Grade implements Serializable {

    private String gradeId,name,status;
    private List<Classz> detail;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Classz> getDetail() {
        return detail;
    }

    public void setDetail(List<Classz> detail) {
        this.detail = detail;
    }
}
