package com.zjhz.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 *班级德育删除
 */
public class DeleteListBean implements Serializable {
    public List<DeleteBean> items;  // 删除集合

    public DeleteListBean(List<DeleteBean> items) {
        this.items = items;
    }
}
