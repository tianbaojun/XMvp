package com.zjhz.teacher.NetworkRequests.retrofit;

import com.zjhz.teacher.NetworkRequests.CommonUrl;

import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by Administrator on 2017/8/16.
 */

public class Api {
    public static final String API_BASE_URL = CommonUrl.BASEURL_TEST+"/";

    private static ReService reService;

    public static ReService getLoginService() {
        if (reService == null) {
            synchronized (Api.class) {
                if (reService == null) {
                    reService = XApi.getInstance().getRetrofit(API_BASE_URL, true).create(ReService.class);
                }
            }
        }
        return reService;
    }
}
