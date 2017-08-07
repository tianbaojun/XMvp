package com.zjhz.teacher.bean;

import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class PhotoListBean implements Serializable{

    private int totalCount;

    private List<LocalImageHelper.LocalFile> datas;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<LocalImageHelper.LocalFile> getDatas() {
        return datas;
    }

    public void setDatas(List<LocalImageHelper.LocalFile> data) {
        this.datas = data;
    }
}
