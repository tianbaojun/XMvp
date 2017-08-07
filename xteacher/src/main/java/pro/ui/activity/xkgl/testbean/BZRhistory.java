package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class BZRhistory implements Serializable {


    private List<History> historyList;
    private List<Week> weekList;

    public BZRhistory() {
        this.historyList = new ArrayList<>();
        this.weekList = new ArrayList<>();
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public List<Week> getWeekList() {
        return weekList;
    }

    public class Week implements Serializable {
        private String week;
        private int index;
        private String startTime;
        private String endTime;

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    public class History implements Serializable{

        /**
         * studentId : 288114324398936076
         * level : 0
         * courseNo : 6S016
         * name : 史雍淡
         * subjectId : 288073719115419648
         * status : 0
         * subjectName : 体育
         */

        private String studentId;
        private String level;
        private String courseNo;
        private String name;
        private String subjectId;
        private int status;
        private String subjectName,levelName;

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCourseNo() {
            return courseNo;
        }

        public void setCourseNo(String courseNo) {
            this.courseNo = courseNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
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
    }
}
