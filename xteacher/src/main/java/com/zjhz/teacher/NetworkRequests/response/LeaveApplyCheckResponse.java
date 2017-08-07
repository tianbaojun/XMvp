package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 请假申请审批入口
 */
public class LeaveApplyCheckResponse implements Serializable{

    /**
     * code : 0
     * msg :
     * data : {"EntryFlag":true}
     */

    public int code;
    public String msg;
    /**
     * EntryFlag : true
     */

    public DataBean data;

    public static class DataBean implements Serializable {
        public boolean EntryFlag;
    }
}
