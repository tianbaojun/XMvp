package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表
 */
public class LeaveStateSchedule implements Serializable {

    public boolean isVisible = true;
    public String date;
    public List<LeaveStateScheduleBean> lists = new ArrayList<>();

}
