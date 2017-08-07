package com.zjhz.teacher.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.response.UserLogin;

/**
 * Created by Administrator on 2016/6/1.
 */
public class SharePreCache {
    /**
     * 获取系统屏幕的宽和高
     *
     * @param context
     * @return "宽;高" 单位是px
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */

    public static int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //隐常键盘
    public static void hintInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void login(UserLogin userLogin){
        SharedPreferencesUtils.putSharePrefString(ConstantKey.AccountKey,userLogin.getAccount());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.PwdKey,userLogin.getPwd());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.UserIdKey,userLogin.getUserId());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.NickNameKey,userLogin.getNickName());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.UserNameKey,userLogin.getUserName());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.PhotoUrlKey,userLogin.getPhotoUrl());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.PhoneNoKey,userLogin.getPhoneNo());
        if (userLogin.getRoles() != null && userLogin.getRoles().size() > 0){
            SharedPreferencesUtils.putSharePrefString(ConstantKey.RoleKey,userLogin.getRoles().get(0).getRoleId());
            SharedPreferencesUtils.putSharePrefString(ConstantKey.RoleType,userLogin.getRoles().get(0).getRoleType());
        }
        SharedPreferencesUtils.putSharePrefString(ConstantKey.SchoolIdKey,userLogin.getSchoolId());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.SchoolNameKey,userLogin.getSchoolName());
        SharedPreferencesUtils.putSharePrefString(ConstantKey.teacherIdKey,userLogin.getTeacherId());
        SharedPreferencesUtils.putSharePrefBoolean(ConstantKey.isRemberPwdKey,userLogin.isRemberPwd());
        SharedPreferencesUtils.putSharePrefBoolean(ConstantKey.isAutoLogin,userLogin.isAutoLogin());
    }

    public static void logOut(){
//        SharedPreferencesUtils.putSharePrefString(ConstantKey.AccountKey,"");
        if (!SharedPreferencesUtils.getSharePrefBoolean(ConstantKey.isRemberPwdKey,false)){
            SharedPreferencesUtils.putSharePrefString(ConstantKey.PwdKey,"");
        }
        SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.UserIdKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.NickNameKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.UserNameKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.PhotoUrlKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.PhoneNoKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.RoleKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.SchoolIdKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.SchoolNameKey,"");
        SharedPreferencesUtils.putSharePrefString(ConstantKey.jobNumberKey,"");
        SharedPreferencesUtils.putSharePrefBoolean(ConstantKey.isupdateheadKey,false);
    }

    public static boolean isEmpty(String str){
        return str == null || "".equals(str) || "null".equals(str);
    }
}
