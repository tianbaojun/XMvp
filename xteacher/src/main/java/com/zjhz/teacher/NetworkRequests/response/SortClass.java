package com.zjhz.teacher.NetworkRequests.response;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/9/6.
 */
public class SortClass implements Comparator{
    @Override
    public int compare(Object lhs, Object rhs) {
        TeacherListBean teacherListBean = (TeacherListBean) lhs;
        TeacherListBean teacherListBean1 = (TeacherListBean) rhs;
        int flag = teacherListBean.getDutyTime().compareTo(teacherListBean1.getDutyTime());
        return flag;
    }
}
