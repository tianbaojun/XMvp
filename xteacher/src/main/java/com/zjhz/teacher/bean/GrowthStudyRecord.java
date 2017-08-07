package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 *班级德育删除
 */
public class GrowthStudyRecord implements Serializable {


    /**
     * classRanking : 3
     * gradeRanking : 3
     * list : [{"YEAR":"SCHOOL_CALENDAR_YEAR_1","classId":"364319293564784640","gradeId":"364286378374074368","score":7,"semester":"SCHOOL_CALENDAR_SEMESTER_1","studentName":"吕诚翰","stuscoreClassId":"374010072004562946","stuscoreRange":"SCHOOL_SCORE_RANGE_1","stuscoreSchemaId":"374010071874539520","stuscoreTime":"2017-04-24 00:00:00","stuscoreType":"SCHOOL_SCORE_TYPE_1","subjectId":"364295028241076224","subjectName":"数学"}]
     * stuscoreTypeName : 期末考试
     * totalScore : 7.0
     */

    private int classRanking;
    private int gradeRanking;
    private String stuscoreTypeName;
    private String totalScore;
    private List<ListBean> list;

    public String getClassRanking() {
        return "班级排名："+classRanking;
    }

    public void setClassRanking(int classRanking) {
        this.classRanking = classRanking;
    }

    public String getGradeRanking() {
        return "年级排名："+gradeRanking;
    }

    public void setGradeRanking(int gradeRanking) {
        this.gradeRanking = gradeRanking;
    }

    public String getStuscoreTypeName() {
        return stuscoreTypeName;
    }

    public void setStuscoreTypeName(String stuscoreTypeName) {
        this.stuscoreTypeName = stuscoreTypeName;
    }

    public String getTotalScore() {
        return "总分："+totalScore+"分";
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * YEAR : SCHOOL_CALENDAR_YEAR_1
         * classId : 364319293564784640
         * gradeId : 364286378374074368
         * score : 7
         * semester : SCHOOL_CALENDAR_SEMESTER_1
         * studentName : 吕诚翰
         * stuscoreClassId : 374010072004562946
         * stuscoreRange : SCHOOL_SCORE_RANGE_1
         * stuscoreSchemaId : 374010071874539520
         * stuscoreTime : 2017-04-24 00:00:00
         * stuscoreType : SCHOOL_SCORE_TYPE_1
         * subjectId : 364295028241076224
         * subjectName : 数学
         */

        private String YEAR;
        private String classId;
        private String gradeId;
        private int score;
        private String semester;
        private String studentName;
        private String stuscoreClassId;
        private String stuscoreRange;
        private String stuscoreSchemaId;
        private String stuscoreTime;
        private String stuscoreType;
        private String subjectId;
        private String subjectName;

        public String getYEAR() {
            return YEAR;
        }

        public void setYEAR(String YEAR) {
            this.YEAR = YEAR;
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

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getStuscoreClassId() {
            return stuscoreClassId;
        }

        public void setStuscoreClassId(String stuscoreClassId) {
            this.stuscoreClassId = stuscoreClassId;
        }

        public String getStuscoreRange() {
            return stuscoreRange;
        }

        public void setStuscoreRange(String stuscoreRange) {
            this.stuscoreRange = stuscoreRange;
        }

        public String getStuscoreSchemaId() {
            return stuscoreSchemaId;
        }

        public void setStuscoreSchemaId(String stuscoreSchemaId) {
            this.stuscoreSchemaId = stuscoreSchemaId;
        }

        public String getStuscoreTime() {
            return stuscoreTime;
        }

        public void setStuscoreTime(String stuscoreTime) {
            this.stuscoreTime = stuscoreTime;
        }

        public String getStuscoreType() {
            return stuscoreType;
        }

        public void setStuscoreType(String stuscoreType) {
            this.stuscoreType = stuscoreType;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }
    }
}
