package com.zjhz.teacher.NetworkRequests;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zjhz.teacher.NetworkRequests.request.FoodDetailParams;
import com.zjhz.teacher.NetworkRequests.response.ErrorUtils;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.base.XzRequest;
import com.zjhz.teacher.ui.activity.LoginActivity;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 网络请求封装
 */
public class NetworkRequest extends XzRequest {

    public static final String FROMCODE = "From_Code";

    public static void request(Object params, final String action, final String type) {
        NetworkRequest.request(params, action, type, -1);
    }


    public static void request(Object params, final String action, final String type, int urlPort) {
        String url = null;
        if (urlPort == -1) {
            url = CommonUrl.BASEURL;
        } else {
            url = CommonUrl.BASEURL.split(":")[0] + ":" + CommonUrl.BASEURL.split(":")[1];
            url += ":" + urlPort + "/api";
        }
        final String baseUrl = url;
        final String str = GsonUtils.toJson(params);
        StringRequest stringRequestPost = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                EventBus.getDefault().post(new EventCenter<Object>(type, object));
                            } else if (object.optInt("code") == 1) {
                                if (!ConstantKey.isLogin) {
                                    ConstantKey.isLogin = true;
                                    exit();
                                    exitAlias();
                                    SharePreCache.logOut();
                                    AppContext.getInstance().finishAllActivitys();
                                    SharedPreferencesUtils.putSharePrefBoolean("net_base", true);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            } else if (object.optInt("code") == 1009 || object.optInt("code") == 8095 || object.optInt("code") == 1005) {
                                EventBus.getDefault().post(new EventCenter<Object>(type, object));
                            } else {
                                object.put(FROMCODE, type);
                                EventBus.getDefault().post(new EventCenter<Object>(Config.NOSUCCESS, object));
                            }
                            Log.e("main", "请求参数：" + str + "\r\nurl:" + baseUrl + "\r\nmethod:" + action + "\r\neventbusCode:" + type);
                            LogUtil.json("请求数据：", response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showShort("数据异常");
                            EventBus.getDefault().post(new EventCenter<Object>(Config.ERROR_JSON, new ErrorUtils("json解析异常")));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("网络请求失败：", error + "");
                ToastUtils.showShort("网络异常");
                EventBus.getDefault().post(new EventCenter<Object>(Config.ERROR, new ErrorUtils("网络请求失败")));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("method", action);
                if (!TextUtils.isEmpty(str)) {
                    map.put("params", str);
                    LogUtil.e("网络请求参数", "------传参------");
                }
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("token", SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey));
                Log.e("token", SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey));
                return map;
            }
        };
        stringRequestPost.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
//        stringRequestPost.setRetryPolicy(new DefaultRetryPolicy(0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequestPost);
    }

    private static void exit() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.e("NetworkRequest", "退出成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                LogUtil.d("NetworkRequest", "退出失败！");
            }
        });
    }

    private static void exitAlias() {
        JPushInterface.setAlias(context, "", new TagAliasCallback() {

            @Override
            public void gotResult(int code, String alias, Set<String> tags) {
                String logs;
                switch (code) {
                    case 0:
                        logs = "Set tag and alias success" + alias;
                        Log.i("NetworkRequest", logs);
                        break;
                    default:
                        logs = "Failed with errorCode = " + code;
                        Log.e("NetworkRequest", logs);
                }
            }
        });
    }

    /**
     * GET请求
     *
     * @param param 请求参数
     * @param url   请求链接
     * @param type  类型
     */
    public static void requestGet(FoodDetailParams param, String url, final String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.e("响应数据：", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                EventBus.getDefault().post(new EventCenter<Object>(type, object));
                            } else {
                                EventBus.getDefault().post(new EventCenter<Object>(Config.NOSUCCESS, object));
                            }
                            LogUtil.e("请求数据：", response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            EventBus.getDefault().post(new EventCenter<Object>(Config.ERROR, new ErrorUtils("网络请求失败")));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("请求错误数据：", error.getMessage());
                EventBus.getDefault().post(new EventCenter<Object>(Config.ERROR, new ErrorUtils("网络请求失败")));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("token", SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey));
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

}
