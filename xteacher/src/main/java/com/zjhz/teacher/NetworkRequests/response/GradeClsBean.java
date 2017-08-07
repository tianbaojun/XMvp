package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**年级班级
 * Created by Administrator on 2016/7/6.
 */
public class GradeClsBean implements Serializable{
    private String name = "";
    private String gradeId = "";
    private List<ClassesBeans> detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public List<ClassesBeans> getDetail() {
        return detail;
    }

    public void setDetail(List<ClassesBeans> detail) {
        this.detail = detail;
    }

}
