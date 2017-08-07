package com.zjhz.teacher.ui.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import com.zjhz.teacher.R;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-3
 * Time: 15:57
 * Description: 电话弹窗
 */
public class DialogPhone extends Dialog implements View.OnClickListener {

    private Window window = null;
    private Context context;

    public DialogPhone(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        showDialog();
    }

    public void showDialog() {
        setContentView(R.layout.dialog_phone);
        windowDeploy();
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_phone_cancle).setOnClickListener(this);
        findViewById(R.id.dialog_phone_ok).setOnClickListener(this);
        show();
    }

    public void windowDeploy() {
        window = getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_phone_cancle:
                break;
            case R.id.dialog_phone_ok:
                Intent intentPhono = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-600-7909"));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(intentPhono);
                break;
        }
        dismiss();
    }
}
