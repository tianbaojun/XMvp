package com.zjhz.teacher.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zjhz.teacher.base.XzRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-09
 * Time: 15:57
 * Description:  专业下载，因为Volley不适合下载大型文件
 */
public class HttpRequest extends XzRequest {

    public static InputStream get(String url) {
        try {
            URL urlPath = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlPath.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            httpURLConnection.connect();
            InputStream inputStream = null;
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("连接超时", "连接超时,服务器被关闭.");
            return null;
        }
    }

    public static InputStream post(String url, String params){
        URL realurl = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        String encoding="UTF-8";
        LogUtil.e("版本更新请求参数 = ",params);
        try {
            byte[] data = params.getBytes(encoding);
            realurl = new URL(url);
            conn = (HttpURLConnection) realurl.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.connect();
            OutputStream outStream = conn.getOutputStream();
            outStream.write(data);
            outStream.flush();
            outStream.close();
            in = conn.getInputStream();
        } catch (MalformedURLException eio) {

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    private static String json;
    public static String initStringRequestPost(String url, final String param) {
        StringRequest stringRequestPost = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.e("获取到的数据", response);
                        json = response;
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object != null) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("获取到的数据", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("params", param);
                return map;
            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                Map<String,String> map = new HashMap<>();
//                map.put("token",SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey,""));
//                return map;
//            }
        };
        stringRequestPost.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        mQueue.add(stringRequestPost);
        return json;
    }
}
