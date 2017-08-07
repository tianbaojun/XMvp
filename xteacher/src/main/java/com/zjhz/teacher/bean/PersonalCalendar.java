package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 个人行事历
 */
public class PersonalCalendar  implements Serializable {

    public int image;
    public String text;
    public String time;
    public boolean tag;   // 判断标记
    public int type;  // 判断级别
    public String id;  // id
    public int priority;  //
    public String summary;  //
}
