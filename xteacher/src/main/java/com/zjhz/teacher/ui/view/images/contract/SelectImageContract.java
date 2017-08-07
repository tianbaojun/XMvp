/*
 * 源文件名：SelectImageContract
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：图片选择器建立契约关系，将权限操作放在Activity，具体数据放在Fragment
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.view.images.contract;

import com.zjhz.teacher.ui.view.images.activity.SelectImageActivity;

public interface SelectImageContract {
    interface Operator {
        void requestCamera();

        void requestExternalStorage();

        void onBack();

        void setDataView(View view);

        SelectImageActivity.Callback getCallback();

        SelectImageActivity.Config getConfig();
    }

    interface View {

        void onOpenCameraSuccess();

        void onCameraPermissionDenied();
    }
}
