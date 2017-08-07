package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;
import java.util.List;

/**
 * 当前值日值周的数据
 * Created by Administrator on 2016/6/29.
 */
public class CurrentWeekBean implements Serializable{
    private List<TeacherListBean> Lists;

    public List<TeacherListBean> getLists() {
        return Lists;
    }

    public void setLists(List<TeacherListBean> lists) {
        Lists = lists;
    }
}
