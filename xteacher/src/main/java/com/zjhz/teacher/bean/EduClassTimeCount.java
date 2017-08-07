package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */

public class EduClassTimeCount implements Serializable {


    private String classId,className;
    private double total,individualMoralTotal,classMoralTotal;
    private List<IndividualMoral> individualMoral;
    private List<ClassMoral> classMoral;

    public String getTotal() {
        return String.valueOf(Math.round(total * 10) / 10.0);
    }

    public String getClassName() {
        return className;
    }

    public String getIndividualMoralTotal() {
        return String.valueOf(Math.round(individualMoralTotal * 10) / 10.0);
    }

    public String getClassMoralTotal() {
        return String.valueOf(Math.round(classMoralTotal * 10) / 10.0);
    }

    public List<IndividualMoral> getIndividualMoral() {
        return individualMoral;
    }

    public List<ClassMoral> getClassMoral() {
        return classMoral;
    }
}
