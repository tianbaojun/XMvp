package com.zjhz.teacher.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjhz.teacher.R;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description:
 */
public class ToastUtils {

    public static Context sContext;
    private static String lastToast;
    private static long lastToastTime;

    private ToastUtils() {}

    public static void register(Context context) {
        sContext = context.getApplicationContext();
    }

    private static void check() {
        if (sContext == null) {
            throw new NullPointerException(
                    "Must initial call ToastUtils.register(Context context) in your " +
                            "<? " +
                            "extends Application class>");
        }
    }

    public static void showToast(String message, int duration, int icon,
                                 int gravity) {
        if (message != null && !message.equalsIgnoreCase("") && !message.contains("token")&&message.length()<50&&!message.equals("error")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToast)
                    || Math.abs(time - lastToastTime) > 2000) { //如果两次连续显示的toast内容不相同且间隔时间大于2秒
                View view = LayoutInflater.from(sContext).inflate(
                        R.layout.view_toast, null);
                ((TextView) view.findViewById(R.id.title_tv)).setText(message);
                if (icon != 0) {
                    ((ImageView) view.findViewById(R.id.icon_iv))
                            .setImageResource(icon);
                    view.findViewById(R.id.icon_iv)
                            .setVisibility(View.VISIBLE);
                }
                Toast toast = new Toast(sContext);
                toast.setView(view);
                if (gravity == Gravity.CENTER) {
                    toast.setGravity(gravity, 0, 0);
                } else {
                    toast.setGravity(gravity, 0, 35);
                }

                toast.setDuration(duration);
                toast.show();
                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }


    public static void showShort(int resId) {
        check();
        String message = sContext.getResources().getString(resId);
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }


    public static void showShort(String message) {
        check();
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }


    public static void showLong(int resId) {
        check();
        String message = sContext.getResources().getString(resId);
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }


    public static void showLong(String message) {
        check();
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }


    public static void showLongX2(String message) {
        showLong(message);
        showLong(message);
    }


    public static void showLongX2(int resId) {
        showLong(resId);
        showLong(resId);
    }


    public static void showLongX3(int resId) {
        showLong(resId);
        showLong(resId);
        showLong(resId);
    }


    public static void showLongX3(String message) {
        showLong(message);
        showLong(message);
        showLong(message);
    }
    public static void toast(String message) {
//        Toast	toast = Toast.makeText(sContext,
//                message, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        LinearLayout toastView = (LinearLayout) toast.getView();
//        ImageView imageCodeProject = new ImageView(sContext);
////        imageCodeProject.setImageResource(R.mipmap.ic_launcher);
//        toastView.addView(imageCodeProject, 0);
//        toast.show();
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
//        View layout = LayoutInflater.from(sContext).inflate(R.layout.custom_toast, null);
//        // set a message
//        TextView toastText = (TextView) layout.findViewById(R.id.toast_text);
//        toastText.setText(message);
//
//        // Toast...
//        Toast toast = new Toast(sContext);
//        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
////        toast.setDuration(duration);
//        toast.setView(layout);
//        toast.show();
    }
}
