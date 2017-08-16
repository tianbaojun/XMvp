package com.zjhz.teacher.NetworkRequests.response;

import com.zjhz.teacher.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class SchoolListBean extends BaseModel implements Serializable {
    private String schoolId = "";
    private String schoolName = "";
    private boolean isSelect = false;
    private List<SchoolListBean> been;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public List<SchoolListBean> getBeen() {
        return been;
    }

    public void setBeen(List<SchoolListBean> been) {
        this.been = been;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
