package com.zjhz.teacher.ui.delegate;

import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.Serializable;

/**
 * Created by zzd on 2017/5/3.
 */

public interface IDrawerLayout {
    DrawerLayout getDrawerLayout();

    ViewGroup getDeleteLayout();

    Serializable getDataBean();
}
