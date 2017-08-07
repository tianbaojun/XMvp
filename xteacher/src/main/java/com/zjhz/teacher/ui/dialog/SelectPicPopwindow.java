/*
 * 源文件名：SelectPicPopwindow
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：图片上传选择弹出框
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.dialog;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.PhotoListBean;
import com.zjhz.teacher.ui.activity.ImageListActivity;
import com.zjhz.teacher.utils.ToastUtils;

public class SelectPicPopwindow {
    public static Uri photoUri;
    private static Activity activity;
    public static int REQUEST_CODE_GETIMAGE_BYCROP = 2;
    public static int SELECT_PIC_BY_TACK_PHOTO = 3;

    public SelectPicPopwindow(Activity activity) {
        SelectPicPopwindow.activity = activity;
    }

    public static PopupWindow makePopwindow(final PhotoListBean bean){
        View view1 = LayoutInflater.from(activity).inflate(R.layout.activity_select_pic_popup_window, null);
        PopupWindow popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
        popupWindow.setOutsideTouchable(false);
        final PopupWindow finalPopupWindow = popupWindow;
        view1.findViewById(R.id.pic_headimg_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalPopupWindow.dismiss();
            }
        });
        view1.findViewById(R.id.pic_headimg_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPhoto();
                finalPopupWindow.dismiss();
            }
        });
        view1.findViewById(R.id.pic_head_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, ImageListActivity.class);
                intent.putExtra("bean", bean);
                activity.startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCROP);
                finalPopupWindow.dismiss();
            }
        });
        return popupWindow;
    }
    private static void toPhoto() {
        String SDState = Environment.getExternalStorageState(); // 执行拍照前，应该先判断SD卡是否存在
        if (!SDState.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.toast("内存卡不存在");
            return;
        }
        try {
            photoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            if (photoUri != null) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                i.putExtra("return-data", true);
                activity.startActivityForResult(i, SELECT_PIC_BY_TACK_PHOTO);
            } else {
                ToastUtils.toast("发生意外photoUri=null，无法写入相册");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Exception:" + e.getMessage());
            ToastUtils.toast("发生意外，无法写入相册");
        }
    }
}
