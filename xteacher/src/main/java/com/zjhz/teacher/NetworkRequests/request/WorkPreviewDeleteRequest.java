package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 作业（详情）预览（删除）
 */
public class WorkPreviewDeleteRequest implements Serializable {

    public String homeworkId;
    public String replyId;

    public WorkPreviewDeleteRequest(String homeworkId, String replyId) {
        this.homeworkId = homeworkId;
        this.replyId = replyId;
    }
}
