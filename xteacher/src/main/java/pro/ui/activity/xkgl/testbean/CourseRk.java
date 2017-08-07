package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class CourseRk implements Serializable{

    /**
     * optAddr : 三年一班教室
     * weekNum : 20
     * teacherId : 288078455692595213
     * stuNum : 14
     * teacherName : 李梅梅
     * stuMax : 45
     * courseNo : 6S022
     * optId : 401955842611482624
     * subjectName : 语文
     */

    private String optAddr;
    private int weekNum;
    private String teacherId;
    private int stuNum;
    private String teacherName;
    private int stuMax;
    private String courseNo;
    private String optId;
    private String subjectName;

    public String getOptAddr() {
        return optAddr;
    }

    public void setOptAddr(String optAddr) {
        this.optAddr = optAddr;
    }

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
