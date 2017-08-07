package com.zjhz.teacher.hx.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.hx.controller.EaseUI;
import com.zjhz.teacher.hx.domain.EaseUser;
import com.zjhz.teacher.utils.SharedPreferencesUtils;


public class EaseUserUtils {
    
    static EaseUI.EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        return null;
    }

    public static String head;
    public static String name;

    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(String type,Context context, String username, ImageView imageView){
        if ("1".equals(type)) {
            String headUrl = SharedPreferencesUtils.getSharePrefString(ConstantKey.PhotoUrlKey);
            Glide.with(context).load(headUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.huan_xin).into(imageView);
        }else {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getAvatar() != null) {
                try {
                    int avatarResId = Integer.parseInt(user.getAvatar());
                    Glide.with(context).load(avatarResId).into(imageView);
                } catch (Exception e) {
                    Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.huan_xin).into(imageView);
                }
            } else {
                Glide.with(context).load(R.mipmap.huan_xin).into(imageView);
            }
        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
            EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    /**
     * set user's nickname
     */
    public static void setUserNickNameAvater(String username,TextView textView){
        if(textView != null){
            String nickName = "";
            if (AppContext.getInstance().hxId != null && AppContext.getInstance().hxId.size() > 0) {
                for (int i = 0; i < AppContext.getInstance().hxId.size(); i++) {
                    if (username.equals(AppContext.getInstance().hxId.get(i).id)) {
                        nickName = AppContext.getInstance().hxId.get(i).name;
//                        GlideUtil.loadImageHead(AppContext.getInstance().hxId.get(i).headImage,imageview);
                    }
                }
            }else{
                nickName = username;
            }
        	textView.setText(nickName);
        }
    }
    
}
