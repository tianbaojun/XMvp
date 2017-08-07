/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zjhz.teacher.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLifeCycle;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zjhz.teacher.BuildConfig;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.base.XzRequest;
import com.zjhz.teacher.bean.ClassAndGradeEducationListCheckBean;
import com.zjhz.teacher.bean.Grade;
import com.zjhz.teacher.bean.SocketBean;
import com.zjhz.teacher.bean.StudentsCurrentPositionClassGrade;
import com.zjhz.teacher.bean.SubjectBeansBean;
import com.zjhz.teacher.hx.controller.EaseUI;
import com.zjhz.teacher.hx.domain.EaseUser;
import com.zjhz.teacher.hx.model.EaseNotifier;
import com.zjhz.teacher.tinker.Log.MyLogImp;
import com.zjhz.teacher.tinker.util.SampleApplicationContext;
import com.zjhz.teacher.tinker.util.TinkerManager;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;
import com.zjhz.teacher.utils.CrashHandler;
import com.zjhz.teacher.utils.DeviceUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * because you can not use any other class in your application, we need to
 * move your implement of Application to {@link ApplicationLifeCycle}
 * As Application, all its direct reference class should be in the main dex.
 *
 * We use tinker-android-anno to make sure all your classes can be patched.
 *
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 *
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 *
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 *
 * loadVerifyFlag: whether check files' md5 on the load bespeakTime, defualt it is false.
 *
 * Created by zzd on 12/4/17.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.zjhz.teacher.app.SampleApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class AppContext extends DefaultApplicationLike {
    private static final String TAG = "Tinker.AppContext";
    public static String currentUserNick = "";
    public static String baiduMap_Version = "";
    private static AppContext instance;
    public final String PREF_USERNAME = "username";// login user name
    public ArrayList<SocketBean> hxId = new ArrayList<>();
    public ArrayList<SocketBean> clickHx = new ArrayList<>();
    public ArrayList<String> persons = new ArrayList<>();
    public List<EMMessage> messageHxs = new ArrayList<>();
    public Map<String, EaseUser> fly = new HashMap<>();
    public ArrayList<String> grades = new ArrayList<>();
    public ArrayList<ArrayList<String>> clazzs = new ArrayList<>();
    public ArrayList<StudentsCurrentPositionClassGrade> clazzsGrades = new ArrayList<>();
    public List<Grade> gradeBeans = new ArrayList<>();
    public ArrayList<String> eProject = new ArrayList<>();
    public List<SubjectBeansBean> eduProjects = new ArrayList<>();//德育方案集合
    public List<ClassAndGradeEducationListCheckBean> eduCheckMore = new ArrayList<>();
    public List<ClassAndGradeEducationListCheckBean> eduCheck = new ArrayList<>();
    public boolean isEnabled = true;
    EaseUI easeUI = EaseUI.getInstance();
    private Map<String, Activity> activitys = new HashMap<>();
    //singleton
    private SocketBean socketBean;
    private long loginUid;
    private SDKReceiver mReceiver;

    public AppContext(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                      long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * 单例AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public static GlideUrl getGlideUrlByUser(String url) {
        if (AppContext.getInstance().isLogin()) {
            return new GlideUrl(url,
                    new LazyHeaders
                            .Builder()
                            .addHeader("Cookie", SharedPreferencesUtils.getSharePrefString("cookie"))
                            .build());
        } else {
            return new GlideUrl(url);
        }
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        SampleApplicationContext.application = getApplication();
        SampleApplicationContext.context = getApplication();
        TinkerManager.setTinkerApplicationLike(this);

//        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onCreate() {
        //设置视频播放的速度声音同步
        VideoOptionModel videoOptionModel =
                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 50);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
        SDKInitializer.initialize(getApplication());
        super.onCreate();
        instance = this;
        GlideUtil.getContext(this.getApplication());
        XzRequest mXzRequest = new XzRequest();
        mXzRequest.request(this.getApplication());
        DeviceUtil.getContext(this.getApplication());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this.getApplication());
        if (!BuildConfig.DEBUG)
            CrashHandler.getInstance().init(this.getApplication());
        ToastUtils.register(this.getApplication());
        SharedPreferencesUtils.getInstance("fly_xz", this.getApplication());
        SharedPreferencesUtils.putSharePrefBoolean("net_base", true);
        LogUtil.e("AppContext boolean = ", SharedPreferencesUtils.getSharePrefBoolean("net_base", true) + "");
        LocalImageHelper.init(this.getApplication());
        EaseUI.getInstance().init(this.getApplication(), null);

        if (BuildConfig.DEBUG) {
            String baseUrl = SharedPreferencesUtils.getSharePrefString(Config.BASE_URL);
            if (!TextUtils.isEmpty(baseUrl)) {
                CommonUrl.BASEURL = baseUrl;
                ToastUtils.showShort(CommonUrl.BASEURL);
            }
        }

        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });


        initEngineManager(this.getApplication());
        ZXingLibrary.initDisplayOpinion(this.getApplication());
    }

    /**
     * 保存登录信息
     *
     */
    @SuppressWarnings("serial")
    public void saveUserInfo() {
        this.loginUid = 101;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
    }

    /**
     * 判断是否登录，返回登录的uid
     * @return
     */
    public boolean isLogin() {
        return    loginUid != 0;
    }

    /**
     * 得到用户信息?
     * @param username 用户名
     * @return
     */
    public EaseUser getUserInfo(String username) {
        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            EaseUser currentUserInfo = new EaseUser(username);
            return currentUserInfo;
        }
        EaseUser easeUser = fly.get(username);
        LogUtil.e("对应的id",username + "     id对应的对象 = " + GsonUtils.toJson(easeUser));
        return easeUser;
    }


    /**
     * 得到缓存路径
     * @return
     */
    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getApplication().getExternalCacheDir();
        else
            cacheDir = getApplication().getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * 添加activity 到activities集合当中
     * @param Tag activity 名称 标志
     * @param activity context
     */
    public void addActivity(String Tag, Activity activity) {
        if (activitys != null && !activitys.containsValue(activity)) {
            activitys.put(Tag, activity);
        }
    }

    /**
     * 结束所有activities
     */
    public void finishAllActivitys() {
        if (activitys != null && activitys.size() > 0) {
            for (String key : activitys.keySet()) {
                activitys.get(key).finish();
            }
        }
    }

    private  void  initEngineManager( Context context ) {
        SDKInitializer.initialize(this.getApplication());
        baiduMap_Version = VersionInfo.getApiVersion();
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        getApplication().registerReceiver(mReceiver, iFilter);
    }

    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                LogUtil.e("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                LogUtil.e("key 验证成功! 功能可以正常使用");
//                ToastUtils.toast("key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                LogUtil.e("网络出错");
            }
        }
    }

}
