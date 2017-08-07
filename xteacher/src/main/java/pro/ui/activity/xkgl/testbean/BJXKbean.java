package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class BJXKbean implements Serializable {


    /**
     * stuList : [{"studentId":"288114322943512593","name":"蔡楚","optId":"401867439463206912","subjectName":"音乐"}]
     * count : 1
     * subjectName : 音乐
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

