package com.zjhz.teacher.ui.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import java.io.File;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 图片选择照相
 */
public class SelectPicPopupWindow extends Activity implements View.OnClickListener{

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic_popup_window);
        findViewById(R.id.pic_head_photo).setOnClickListener(this);
        findViewById(R.id.pic_headimg_photo).setOnClickListener(this);
        findViewById(R.id.pic_headimg_cancle).setOnClickListener(this);
        Intent intent = getIntent();
        code = intent.getStringExtra("int");
    }
    /***使用照相机拍照获取图片*/
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***使用相册中的图片*/
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    private static final int CUT_PHOTO = 3;
    private Uri photoUri;
    /**
     *
     * 系统相机拍照
     */
    private void takePhoto() {
        String SDState = Environment.getExternalStorageState(); // 执行拍照前，应该先判断SD卡是否存在
        if (!SDState.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.toast("内存卡不存在");
            return;
        }
        try {
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            if (photoUri != null) {
                Intent i = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(i, SELECT_PIC_BY_TACK_PHOTO);
            } else {
                ToastUtils.toast("发生意外，无法写入相册");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.toast("发生意外，无法写入相册");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PIC_BY_TACK_PHOTO:
                    beginCrop(photoUri);// 选择自拍结果
                    break;
                case SELECT_PIC_BY_PICK_PHOTO:
                    beginCrop(intent.getData());// 选择图库图片结果
                    break;
                case CUT_PHOTO:
                    handleCrop(intent);
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void beginCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，注意如果return-data=true情况下,其实得到的是缩略图，并不是真实拍摄的图片大小，
        // 而原因是拍照的图片太大，所以这个宽高当你设置很大的时候发现并不起作用，就是因为返回的原图是缩略图，但是作为头像还是够清晰了
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //返回图片数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CUT_PHOTO);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param result
     */
    private void handleCrop(Intent result) {
        Bundle extras = result.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//			LogUtil.i("KtActivityAccountInfo", photo+"");
//            GlideUtil.saveUserPicToFile(getApplicationContext(), photo, "head_image");
//            personInfoIcon.setImageBitmap(photo);
//            ToastUtils.showShort("图片" + photo);
            try {
                GlideUtil.readAsFile(GlideUtil.Bitmap2IS(photo),new File(Environment.getExternalStorageDirectory()+"/"+ code +".jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new EventCenter<Object>(code));
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic_head_photo:
                Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, SELECT_PIC_BY_PICK_PHOTO);
                break;
            case R.id.pic_headimg_photo:
                takePhoto();
                break;
            case R.id.pic_headimg_cancle:
                finish();
                break;
        }
    }
}
