package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 作业（详情）预览
 */
public class WorkPreviewRequest implements Serializable {

    public String homeworkId;
    public String msgContect;

    public WorkPreviewRequest(String homeworkId, String msgContect) {
        this.homeworkId = homeworkId;
        this.msgContect = msgContect;
    }
}
