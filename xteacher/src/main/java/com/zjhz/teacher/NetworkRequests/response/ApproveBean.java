package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 是否报修审批员
 * Created by Administrator on 2016/7/13.
 */
public class ApproveBean implements Serializable{
    private boolean EntryFlag = false;

    public boolean isEntryFlag() {
        return EntryFlag;
    }

    public void setEntryFlag(boolean entryFlag) {
        EntryFlag = entryFlag;
    }
}
