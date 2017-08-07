package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class BJXKBeanAdmin implements Serializable {

    /**
     * subMap : {"selectList":[{"stuList":[{"studentId":"288114322943512593","name":"蔡楚","optId":"402941428012945408","subjectName":"历史"}],"count":15,"subjectName":"历史"}],"selectNum":31}
     * noSubMap : {"noSelectList":[{"stuList":[{"studentId":"288114322905763842","name":"蔡淡","optId":"0","subjectName":"0"}],"count":44,"subjectName":"0"}],"noSelectNum":44}
     */

    private SubMapBean subMap;
    private NoSubMapBean noSubMap;

    public SubMapBean getSubMap() {
        return subMap;
    }

    public void setSubMap(SubMapBean subMap) {
        this.subMap = subMap;
    }

    public NoSubMapBean getNoSubMap() {
        return noSubMap;
    }

    public void setNoSubMap(NoSubMapBean noSubMap) {
        this.noSubMap = noSubMap;
    }

    public static class SubMapBean implements Serializable  {
        /**
         * selectList : [{"stuList":[{"studentId":"288114322943512593","name":"蔡楚","optId":"402941428012945408","subjectName":"历史"}],"count":15,"subjectName":"历史"}]
         * selectNum : 31
         */

        private int selectNum;
        private List<SelectListBean> selectList;

        public int getSelectNum() {
            return selectNum;
        }

        public void setSelectNum(int selectNum) {
            this.selectNum = selectNum;
        }

        public List<SelectListBean> getSelectList() {
            return selectList;
        }

        public void setSelectList(List<SelectListBean> selectList) {
            this.selectList = selectList;
        }

        public static class SelectListBean implements Serializable  {
            /**
             * stuList : [{"studentId":"288114322943512593","name":"蔡楚","optId":"402941428012945408","subjectName":"历史"}]
             * count : 15
             * subjectName : 历史
             */

            private int count;
            private String subjectName;
            private List<XSXKbean> stuList;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public List<XSXKbean> getStuList() {
                return stuList;
            }

            public void setStuList(List<XSXKbean> stuList) {
                this.stuList = stuList;
            }
        }
    }

    public static class NoSubMapBean implements Serializable  {
        /**
         * noSelectList : [{"stuList":[{"studentId":"288114322905763842","name":"蔡淡","optId":"0","subjectName":"0"}],"count":44,"subjectName":"0"}]
         * noSelectNum : 44
         */

        private int noSelectNum;
        private List<NoSelectListBean> noSelectList;

        public int getNoSelectNum() {
            return noSelectNum;
        }

        public void setNoSelectNum(int noSelectNum) {
            this.noSelectNum = noSelectNum;
        }

        public List<NoSelectListBean> getNoSelectList() {
            return noSelectList;
        }

        public void setNoSelectList(List<NoSelectListBean> noSelectList) {
            this.noSelectList = noSelectList;
        }

        public static class NoSelectListBean implements Serializable  {
            /**
             * stuList : [{"studentId":"288114322905763842","name":"蔡淡","optId":"0","subjectName":"0"}]
             * count : 44
             * subjectName : 0
             */

            private int count;
            private String subjectName;
            private List<XSXKbean> stuList;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public List<XSXKbean> getStuList() {
                return stuList;
            }

            public void setStuList(List<XSXKbean> stuList) {
                this.stuList = stuList;
            }
        }
    }
}

