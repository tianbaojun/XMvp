package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class ScoreRecordbean implements Serializable {

    private String status;

    private List<StuList> stuList = new ArrayList<>();
    private Level commentLevel;

    public String getStatus() {
        return status;
    }

    public List<StuList> getStuList() {
        return stuList;
    }

    public Level getCommentLevel() {
        return commentLevel;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStuList(List<StuList> stuList) {
        this.stuList = stuList;
    }

    public void setCommentLevel(Level commentLevel) {
        this.commentLevel = commentLevel;
    }

    public ScoreRecordbean() {
        stuList = new ArrayList<>();
    }

    public class Level implements Serializable {

        /**
         * level5 : 差
         * level4 : 一般
         * stuCommentId : 402013736551845888
         * createTime : 2017-07-10 23:53:58
         * commentWay : 1
         * schoolId : 288069341826519040
         * updateUser : 272484438485504000
         * createUser : 272484438485504000
         * updateTime : 2017-07-10 23:53:58
         * level1 : 优
         * level3 : 一般
         * level2 : 良
         */

        private String level5;
        private String level4;
        private String stuCommentId;
        private String createTime;
        private int commentWay;
        private String schoolId;
        private String updateUser;
        private String createUser;
        private String updateTime;
        private String level1;
        private String level3;
        private String level2;

        public String getLevel5() {
            return level5;
        }

        public void setLevel5(String level5) {
            this.level5 = level5;
        }

        public String getLevel4() {
            return level4;
        }

        public void setLevel4(String level4) {
            this.level4 = level4;
        }

        public String getStuCommentId() {
            return stuCommentId;
        }

        public void setStuCommentId(String stuCommentId) {
            this.stuCommentId = stuCommentId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getCommentWay() {
            return commentWay;
        }

        public void setCommentWay(int commentWay) {
            this.commentWay = commentWay;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getLevel1() {
            return level1;
        }

        public void setLevel1(String level1) {
            this.level1 = level1;
        }

        public String getLevel3() {
            return level3;
        }

        public void setLevel3(String level3) {
            this.level3 = level3;
        }

        public String getLevel2() {
            return level2;
        }

        public void setLevel2(String level2) {
            this.level2 = level2;
        }
    }

    public class StuList implements Serializable {

        /**
         * studentId : 288114323346165769
         * courseScoreId : 401951863206842368
         * level : 1
         * name : 贾盟新
         * optId : 401816436466520064
         */

        private long studentId;
        private long courseScoreId;
        private String level;
        private String name;
        private long optId;
        private String photoUrl;

        public long getStudentId() {
            return studentId;
        }

        public void setStudentId(long studentId) {
            this.studentId = studentId;
        }

        public long getCourseScoreId() {
            return courseScoreId;
        }

        public void setCourseScoreId(long courseScoreId) {
            this.courseScoreId = courseScoreId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getOptId() {
            return optId;
        }

        public void setOptId(long optId) {
            this.optId = optId;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }

}

