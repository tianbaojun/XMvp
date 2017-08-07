package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class XSXKbean implements Serializable {


    /**
     * studentId : 288114322943512593
     * name : 蔡楚
     * optId : 401867439463206912
     * subjectName : 音乐
     */

    private String studentId;
    private String name;
    private String optId;
    private String subjectName,photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

