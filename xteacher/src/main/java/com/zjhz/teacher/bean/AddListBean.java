package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 *班级德育删除
 */
public class AddListBean implements Serializable {
    public List<AddBean> items;  // 德育集合
    public String studentId;  //督导员id
    public String studentName;  //督导员名字

    public AddListBean(List<AddBean> items) {
        this.items = items;
    }
}
