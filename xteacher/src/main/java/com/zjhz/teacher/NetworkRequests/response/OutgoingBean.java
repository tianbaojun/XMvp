package com.zjhz.teacher.NetworkRequests.response;

import java.io.Serializable;

/**
 * 外出公告
 * Created by Administrator on 2016/6/27.
 */
public class OutgoingBean implements Serializable {
    private String annContent = "";
    private String publishTime = "";
    private String createUser = "";
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnnContent() {
        return annContent;
    }

    public void setAnnContent(String annContent) {
        this.annContent = annContent;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
