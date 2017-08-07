package com.zjhz.teacher.NetworkRequests;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/6/23.
 * @see LoadFile   usage
 */
public class InitOkHttpClient {
    private static OkHttpClient okHttpClient = null;
    private InitOkHttpClient(){}
    public static OkHttpClient getInstance(){
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient();
            //设置超时
            okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        }
        return okHttpClient;
    }
}
