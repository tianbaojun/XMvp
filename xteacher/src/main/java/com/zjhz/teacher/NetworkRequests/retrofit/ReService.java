package com.zjhz.teacher.NetworkRequests.retrofit;

import com.zjhz.teacher.NetworkRequests.retrofit.Model.LoginRespon;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/16.
 */

public interface ReService {
    @GET("{method}")
    Observable<LoginRespon> login(@Path("method") String method, @QueryMap Map<String,String> map);
}
