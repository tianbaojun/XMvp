package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/8.
 */

public class MonthWeekOfTerm implements Serializable {
    private List<MonthOfTerm> monthBetween;
    private List<WeekOfTerm> week;

    public List<MonthOfTerm> getMonthBetween() {
        return monthBetween;
    }

    public List<WeekOfTerm> getWeek() {
        return week;
    }
}
