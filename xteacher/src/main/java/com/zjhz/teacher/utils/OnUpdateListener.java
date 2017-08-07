package com.zjhz.teacher.utils;


import com.zjhz.teacher.bean.UpdateInfo;

/**
 * Created by ShelWee on 14-5-16.
 */
public interface OnUpdateListener {
    /**
     * on start check
     */
    void onStartCheck();

    /**
     * on finish check
     */
    void onFinishCheck(UpdateInfo info, boolean isUpdateLowestVersion);

    void onStartDownload();

    void onDownloading(int progress);

    void onFinshDownload();

}
