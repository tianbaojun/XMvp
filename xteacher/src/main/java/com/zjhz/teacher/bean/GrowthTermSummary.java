package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 *班级德育删除
 */
public class GrowthTermSummary implements Serializable {

    /**
     * list : [{"appraiseId":"392036602001494016","appraiseName":"学期寄语","content":"小米对我说：勤奋不是事业成功的忠实伴侣。 ","id":"392381128633225216","studentId":"288114323098701831"}]
     * title : 学期寄语
     */

    private String title;
    private List<ListBean> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * appraiseId : 392036602001494016
         * appraiseName : 学期寄语
         * content : 小米对我说：勤奋不是事业成功的忠实伴侣。
         * id : 392381128633225216
         * studentId : 288114323098701831
         */

        private String appraiseId;
        private String appraiseName;
        private String content;
        private String id;
        private String studentId;

        public String getAppraiseId() {
            return appraiseId;
        }

        public void setAppraiseId(String appraiseId) {
            this.appraiseId = appraiseId;
        }

        public String getAppraiseName() {
            return appraiseName;
        }

        public void setAppraiseName(String appraiseName) {
            this.appraiseName = appraiseName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }
    }
}
