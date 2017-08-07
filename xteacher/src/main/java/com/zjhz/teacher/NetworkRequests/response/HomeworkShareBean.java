package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by zzd on 2017/7/12.
 */

public class HomeworkShareBean implements Serializable {

    /**
     * homeworkName : 数学作业分享到成长记事
     * praiseLevelName : 良
     * praiseContent : 做的不错
     * praiseLevel : SYS_PRAISE_LEVEL_2
     * content : 分享到成长记事
     */

    private String homeworkName;
    private String praiseLevelName;
    private String praiseContent;
    private String praiseLevel;
    private String content;

    private String pypf;

    public String getHomeworkName() {
        return homeworkName == null? "":homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public String getPraiseLevelName() {
        return praiseLevelName == null? "" : praiseLevelName;
    }

    public void setPraiseLevelName(String praiseLevelName) {
        this.praiseLevelName = praiseLevelName;
    }

    public String getPraiseContent() {
        return praiseContent == null? "": praiseContent;
    }

    public void setPraiseContent(String praiseContent) {
        this.praiseContent = praiseContent;
    }

    public String getPraiseLevel() {
        return praiseLevel == null? "": praiseLevel;
    }

    public void setPraiseLevel(String praiseLevel) {
        this.praiseLevel = praiseLevel;
    }

    public String getContent() {
        return content == null? "":content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPypf() {
        if(praiseLevelName == null)
            praiseLevelName = "";
        if(praiseContent == null)
            praiseContent = "";
        pypf = praiseLevelName + ": " + praiseContent;
        return pypf;
    }

    public void setPypf(String pypf) {
        this.pypf = pypf;
    }
}
