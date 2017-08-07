package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**学年 学期  成绩类型
 * Created by Administrator on 2016/8/30.
 */
public class TermAndTypeParams implements Serializable{
    private String paramKeys;

    public TermAndTypeParams(String paramKeys) {
        this.paramKeys = paramKeys;
    }
}
