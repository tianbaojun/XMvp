package com.zjhz.teacher.bean;

import java.io.Serializable;

/**
 * Created by zzd on 2017/7/27.
 */

public class AudioBean implements Serializable {
    public String attPath;
    public int time;

    public AudioBean(String attPath, int duration) {
        this.attPath = attPath;
        this.time = duration;
    }
}
