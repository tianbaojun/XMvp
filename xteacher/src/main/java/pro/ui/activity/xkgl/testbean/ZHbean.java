package pro.ui.activity.xkgl.testbean;

import com.zjhz.teacher.utils.BaseUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class ZHbean implements Serializable {


    /**
     * supId : 288078455709372427
     * stuNum : 3
     * teacherName : 王喆
     * stuMax : 10
     * courseTypeId : 400414036816039936
     * stuMin : 3
     * typeName : 体育类
     * updateUser : 288078455713566737
     * updateTime : 2017-07-10 10:50:49
     * propName : 基础课程
     * subjectId : 288073718528217088
     * optAddr : A123
     * limMax : 5
     * teacherId : 288078455709372419
     * calendarId : 398552516238774272
     * createTime : 2017-07-10 10:49:58
     * courseNo : a123
     * schoolId : 288069341826519040
     * coursePropId : 400414257906192384
     * createUser : 288078455713566737
     * supName : 王怡
     * optId : 401816436466520064
     * status : 1
     * subjectName : 语文
     */

    private String supId;
    private int stuNum;
    private String teacherName;
    private int stuMax;
    private String courseTypeId;
    private int stuMin;
    private String typeName;
    private String updateUser;
    private String updateTime;
    private String propName;
    private String subjectId;
    private String optAddr;
    private int limMax;
    private String teacherId;
    private String calendarId;
    private String createTime;
    private String courseNo;
    private String schoolId;
    private String coursePropId;
    private String createUser;
    private String supName;
    private String optId;
    private int status;
    private String subjectName;
    private List<GradeBean> gradeList;

    public List<GradeBean> getGradeList() {
        return gradeList;
    }

    public String getGradesName(){
        if(BaseUtil.isEmpty(gradeList)){
            return  "";
        }
        StringBuilder sb = new StringBuilder();
        if (gradeList != null) {
            for (GradeBean bean : gradeList) {
                sb.append(bean.getGradeName() + ",");
            }
        }
        if(sb.length()>0) {
            return sb.substring(0, sb.length() - 1);
        }else {
            return "";
        }
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public int getStuNum() {
        return stuNum;
    }

    public void setStuNum(int stuNum) {
        this.stuNum = stuNum;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getStuMax() {
        return stuMax;
    }

    public void setStuMax(int stuMax) {
        this.stuMax = stuMax;
    }

    public String getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(String courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public int getStuMin() {
        return stuMin;
    }

    public void setStuMin(int stuMin) {
        this.stuMin = stuMin;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getOptAddr() {
        return optAddr;
    }

    public void setOptAddr(String optAddr) {
        this.optAddr = optAddr;
    }

    public int getLimMax() {
        return limMax;
    }

    public void setLimMax(int limMax) {
        this.limMax = limMax;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCoursePropId() {
        return coursePropId;
    }

    public void setCoursePropId(String coursePropId) {
        this.coursePropId = coursePropId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    class GradeBean implements Serializable{

        /**
         * gradeId : 288073659753435136
         * optId : 401980730248269824
         * gradeName : 一年级
         */

        private String gradeId;
        private String optId;
        private String gradeName;

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public String getOptId() {
            return optId;
        }

        public void setOptId(String optId) {
            this.optId = optId;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }
    }
}

