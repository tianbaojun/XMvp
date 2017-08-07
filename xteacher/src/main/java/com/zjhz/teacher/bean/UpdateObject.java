package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3.
 */
public class UpdateObject implements Serializable{
    public int PraiseNum;
    public int ReplyNum;
    public UpdateObject(int praiseNum, int replyNum) {
        PraiseNum = praiseNum;
        ReplyNum = replyNum;
    }
}
