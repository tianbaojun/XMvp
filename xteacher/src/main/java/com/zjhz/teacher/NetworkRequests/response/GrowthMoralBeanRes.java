package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzd on 2017/6/21.
 */

public class GrowthMoralBeanRes implements Serializable{
    /**
     * monthList : [{"day":"04月01日","dayList":[{"checkTime":"04月01日","classId":288113366604451840,"moralId":"307950863517749248","moralName":"个人其他","score":-0.1}],"total":"-0.1"},{"day":"04月09日","dayList":[{"checkTime":"04月09日","classId":288113366604451840,"moralId":"390674459163365376","moralName":"123","score":-6},{"checkTime":"04月09日","classId":288113366604451840,"moralId":"390674459163365376","moralName":"123","score":2}],"total":"-4.0"}]
     * month : 04
     * total : -4.1
     */

    private String month;
    private String total;
    private List<MonthListBean> monthList;

    public String getMonth() {
        return month + "月份";
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<MonthListBean> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<MonthListBean> monthList) {
        this.monthList = monthList;
    }

    public static class MonthListBean implements Serializable{
        /**
         * day : 04月01日
         * dayList : [{"checkTime":"04月01日","classId":288113366604451840,"moralId":"307950863517749248","moralName":"个人其他","score":-0.1}]
         * total : -0.1
         */

        private String day;
        private String total;
        private List<DayListBean> dayList;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<DayListBean> getDayList() {
            return dayList;
        }

        public void setDayList(List<DayListBean> dayList) {
            this.dayList = dayList;
        }

        public static class DayListBean implements Serializable{
            /**
             * checkTime : 04月01日
             * classId : 288113366604451840
             * moralId : 307950863517749248
             * moralName : 个人其他
             * score : -0.1
             */

            private String checkTime;
            private long classId;
            private String moralId;
            private String moralName;
            private float score;
            private String scoreFlag;

            public String getCheckTime() {
                return checkTime;
            }

            public void setCheckTime(String checkTime) {
                this.checkTime = checkTime;
            }

            public long getClassId() {
                return classId;
            }

            public void setClassId(long classId) {
                this.classId = classId;
            }

            public String getMoralId() {
                return moralId;
            }

            public void setMoralId(String moralId) {
                this.moralId = moralId;
            }

            public String getMoralName() {
                return moralName;
            }

            public void setMoralName(String moralName) {
                this.moralName = moralName;
            }

            public float getScore() {
                return score;
            }

            public void setScore(float score) {
                this.score = score;
            }

            public String getScoreFlag() {
                scoreFlag = "";
                if(score >= 0)
                    scoreFlag = "+";
                if(score < 0)
                    scoreFlag = "-";

                return scoreFlag;
            }

            public void setScoreFlag(String scoreFlag) {
                this.scoreFlag = scoreFlag;
            }
        }
    }


//    /**
//     * addList : [{"list":[{"checkTime":"02月01日","classId":288113366667366400,"moralId":"296083597637455872","moralName":"眼保健操检查","score":1}],"month":"2017-02"},{"list":[{"checkTime":"03月05日","classId":288113366667366400,"moralId":"296083597637455872","moralName":"眼保健操检查","score":2}],"month":"2017-03"},{"list":[{"checkTime":"04月01日","classId":288113366667366400,"moralId":"296083597637455872","moralName":"眼保健操检查","score":3}],"month":"2017-04"}]
//     * total : 6.0
//     */
//
//    private String total;
//    private String totalFlg;
//    private int scoreColor;
//    private String month;
//    private List<AddListBean> addList;
//
//    public String getMonth() {
//        return month + "月份";
//    }
//
//    public void setMonth(String month) {
//        this.month = month;
//    }
//
//    public String getTotalFlg() {
//        if(Integer.parseInt(total) > 0 ){
//            totalFlg = "+"+total;
//        }else {
//            totalFlg = total;
//        }
//        return totalFlg;
//    }
//
//    public void setTotalFlg(String totalFlg) {
//        this.totalFlg = totalFlg;
//    }
//
//    public int getScoreColor() {
//        if(Integer.parseInt(total) > 0 ){
//            scoreColor = R.color.growth_moral_plus;
//        }else {
//            scoreColor = R.color.growth_moral_reduce;
//        }
//        return scoreColor;
//    }
//
//    public void setScoreColor(int scoreColor) {
//        this.scoreColor = scoreColor;
//    }
//
//    public String getTotal() {
//        return total;
//    }
//
//    public void setTotal(String total) {
//        this.total = total;
//    }
//
//    public List<AddListBean> getAddList() {
//        return addList;
//    }
//
//    public void setAddList(List<AddListBean> addList) {
//        this.addList = addList;
//    }
//
//    public static class AddListBean implements Serializable{
//        /**
//         * list : [{"checkTime":"02月01日","classId":288113366667366400,"moralId":"296083597637455872","moralName":"眼保健操检查","score":1}]
//         * month : 2017-02
//         */
//
//        private int scoreDotColor;
//        private String month;
//        private List<ListBean> list;
//
//        public int getScoreDotColor() {
//            int score = 0;
//            for(ListBean bean : list){
//                score += bean.score;
//            }
//            if(score > 0 ){
//                scoreDotColor = R.drawable.growth_moral_plus_shape;
//            }else {
//                scoreDotColor =  R.drawable.growth_moral_reduce_shape;
//            }
//            return scoreDotColor;
//        }
//
//        public void setScoreDotColor(int scoreDotColor) {
//            this.scoreDotColor = scoreDotColor;
//        }
//
//        public String getMonth() {
//            return month;
//        }
//
//        public void setMonth(String month) {
//            this.month = month;
//        }
//
//        public List<ListBean> getList() {
//            return list;
//        }
//
//        public void setList(List<ListBean> list) {
//            this.list = list;
//        }
//
//        public static class ListBean implements Serializable{
//            /**
//             * checkTime : 02月01日
//             * classId : 288113366667366400
//             * moralId : 296083597637455872
//             * moralName : 眼保健操检查
//             * score : 1
//             */
//
//            private String checkTime;
//            private long classId;
//            private String moralId;
//            private String moralName;
//            private int score;
//            private String scoreFlag;
//
//            public String getCheckTime() {
//                return checkTime;
//            }
//
//            public void setCheckTime(String checkTime) {
//                this.checkTime = checkTime;
//            }
//
//            public long getClassId() {
//                return classId;
//            }
//
//            public void setClassId(long classId) {
//                this.classId = classId;
//            }
//
//            public String getMoralId() {
//                return moralId;
//            }
//
//            public void setMoralId(String moralId) {
//                this.moralId = moralId;
//            }
//
//            public String getMoralName() {
//                return moralName;
//            }
//
//            public void setMoralName(String moralName) {
//                this.moralName = moralName;
//            }
//
//            public int getScore() {
//                return score;
//            }
//
//            public void setScore(int score) {
//                this.score = score;
//            }
//
//            public String getScoreFlag() {
//                scoreFlag = "";
//                if(score > 0)
//                    scoreFlag = "+";
//                if(score < 0)
//                    scoreFlag = "-";
//
//                return scoreFlag;
//            }
//
//            public void setScoreFlag(String scoreFlag) {
//                this.scoreFlag = scoreFlag;
//            }
//        }
//    }
}
