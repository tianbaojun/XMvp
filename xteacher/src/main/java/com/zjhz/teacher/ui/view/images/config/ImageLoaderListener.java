/*
 * 源文件名：ImageLoaderListener
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述: 图片加载器
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.view.images.config;

import android.widget.ImageView;

public interface ImageLoaderListener {
    void displayImage(ImageView iv, String path);
}
