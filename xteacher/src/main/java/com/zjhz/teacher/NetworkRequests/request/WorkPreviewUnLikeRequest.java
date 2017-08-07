package com.zjhz.teacher.NetworkRequests.request;

import java.io.Serializable;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 作业（详情）预览
 */
public class WorkPreviewUnLikeRequest implements Serializable {

    public String homeworkId;
    public String praiseId;

    public WorkPreviewUnLikeRequest(String homeworkId, String praiseId) {
        this.homeworkId = homeworkId;
        this.praiseId = praiseId;
    }
}
