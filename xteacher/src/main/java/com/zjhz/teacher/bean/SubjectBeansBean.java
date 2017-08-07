package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class SubjectBeansBean implements Serializable {

    public String schemeName; // 方案名称
    public String schemeId; // 方案id
    public String status; //  方案列表需要  查找方案的话要传入status=1启用    status=0保存
    public String meterMode; // meterMode:计分方式：0固定分值   1评分制就是可以输入分数
}
