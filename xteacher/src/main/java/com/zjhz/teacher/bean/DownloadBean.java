package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by zzd on 2017/4/21.
 */

public class DownloadBean implements Serializable {
    public String url;
    public String fileName;

    public DownloadBean() {
    }
    public DownloadBean(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

}
