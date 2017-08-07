package com.zjhz.teacher.base;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;
import com.zjhz.teacher.NetworkRequests.OkHttpStack;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description:
 */
public class XzRequest {

    public static RequestQueue mQueue;
    public static Context context;
    public void request(Context context){
        this.context = context;
//        mQueue = Volley.newRequestQueue(context,new OkHttpStack(new OkHttpClient()));
        mQueue = Volley.newRequestQueue(context);
    }
}
