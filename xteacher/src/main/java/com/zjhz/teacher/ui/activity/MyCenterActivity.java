package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zjhz.teacher.BuildConfig;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.HeadImage;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.SelectPicPopupWindow;
import com.zjhz.teacher.utils.CMCPhotoUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import pro.kit.ViewTools;

/**
 * 个人资料中心
 * Created by xiangxue on 2016/6/1.
 */
public class MyCenterActivity extends BaseActivity implements LoadFile.FileCallBack {

    private final static String TAG = MyCenterActivity.class.getSimpleName();
    private static int PHOTO_REQUEST_CODE = 5;
    private static String headerImg = "headerImg";
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success" + alias;
                    Log.i(TAG, logs);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.sex_tv)
    TextView sexTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.account_tv)
    TextView account_tv;
    @BindView(R.id.school_name_tv)
    TextView school_name_tv;
    @BindView(R.id.jobnumber_tv)
    TextView jobnumber_tv;
    @BindView(R.id.dept_name_tv)
    TextView dept_name_tv;
    @BindView(R.id.env_layout)
    RelativeLayout env_layout;
    @BindView(R.id.env_text)
    TextView env_text;
    private Bitmap bitmap;
    private LoadFile loadFile ;
    private long lastClickTime = 0L;
    private int clickNum;
    private int type = 0;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycenter);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        loadFile = new LoadFile(this);
        initView();
    }

    public void initView() {
        if (BuildConfig.DEBUG) {
            env_layout.setVisibility(View.VISIBLE);
            changeEnv();
        } else {
            env_layout.setVisibility(View.GONE);
        }


        title_tv.setText("个人中心");
        title_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        boolean isupdate = SharedPreferencesUtils.getSharePrefBoolean(ConstantKey.isupdateheadKey,false);
        if (isupdate){
            String imgUrl = Environment.getExternalStorageDirectory() + "/" + headerImg + ".jpg";
            bitmap = CMCPhotoUtil.CompresPhoto(imgUrl,300,300);
            if (bitmap != null){
                userHead.setImageBitmap(bitmap);
            }
        }else {
            String headUrl = SharedPreferencesUtils.getSharePrefString(ConstantKey.PhotoUrlKey);
            if(!SharePreCache.isEmpty(headUrl)){
                GlideUtil.loadImageHead(headUrl,userHead);
            }
        }
        nameTv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.NickNameKey));
        sexTv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherSexKey));
//        account_tv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.AccountKey));
        account_tv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey));
        phoneTv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey));
        school_name_tv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolNameKey));
        dept_name_tv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherDeptKey));
        jobnumber_tv.setText(SharedPreferencesUtils.getSharePrefString(ConstantKey.jobNumberKey));
    }

    @OnClick({R.id.user_head, R.id.nick_rl, R.id.name_rl, R.id.sex_rl, R.id.phone_rl,R.id.schoolname_rl})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.user_head:
                Intent intent0 = new Intent(MyCenterActivity.this, SelectPicPopupWindow.class);
                intent0.putExtra("int", headerImg);
                startActivityForResult(intent0, PHOTO_REQUEST_CODE);
                break;
            case R.id.schoolname_rl:
                startActivity(MySchoolActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_CODE) {
                final List<File> files = new ArrayList<>();
                String imgUrl = Environment.getExternalStorageDirectory() + "/" + headerImg + ".jpg";
                File file = new File(imgUrl);
                bitmap = CMCPhotoUtil.CompresPhoto(imgUrl,300,300);
                if (bitmap != null){
                    CMCPhotoUtil.SaveBitmapFile(bitmap,file);
                    files.add(file);
                }
                if (files.size() > 0){
                    dialog.setMessage("正在上传...");
                    dialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loadFile.uploadFile(files);
                        }
                    }).start();
                }else {
                    ToastUtils.showShort("选取图片失败");
                }
            }
        }
    }

    @Override
    public void onfileComplete(final Response response) {
        if (response.code() == 200){
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                ImageUrls imageUrls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(),ImageUrls.class);
                String urls = "";
                if (imageUrls != null){
                    String f0 = imageUrls.getFile0();
                    if (!SharePreCache.isEmpty(f0)){ urls += f0+",";}
                }
                if (!SharePreCache.isEmpty(urls)){
                    urls = urls.substring(0,urls.length() - 1);
                }
                final String finalUrls = urls;
                HeadImage headImage = new HeadImage(finalUrls,SharedPreferencesUtils.getSharePrefString("login_userid"));
                LogUtil.e("请求参数修改头像",GsonUtils.toJson(headImage));
                NetworkRequest.request(headImage,CommonUrl.loadImageHead,"head_my_center");
                LogUtil.e("头像urls = ",urls);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    ToastUtils.showShort("图片上传失败code="+response.code());
                }
            });
        }
    }

    @Override
    public void onfileErr(Request request) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                ToastUtils.showShort("图片上传失败fail");
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                ToastUtils.toast("网络错误");
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case "head_my_center":
                dialog.dismiss();
                userHead.setImageBitmap(bitmap);
                SharedPreferencesUtils.putSharePrefBoolean(ConstantKey.isupdateheadKey,true);
                ToastUtils.showShort("修改头像成功");
                break;

            case Config.LOGOUT:
                switch (type) {
                    case 4:
                        SharedPreferencesUtils.putSharePrefString(Config.BASE_URL, CommonUrl.BASEURL_WWW);
                        break;
                    case 3:
                        SharedPreferencesUtils.putSharePrefString(Config.BASE_URL, CommonUrl.BASEURL_DEV);
                        break;
                    case 2:
                        SharedPreferencesUtils.putSharePrefString(Config.BASE_URL, CommonUrl.BASEURL_TEST1);
                        break;
                    case 1:
                        SharedPreferencesUtils.putSharePrefString(Config.BASE_URL, CommonUrl.BASEURL_TEST);
                        break;
                }

                dialog.dismiss();
                exit();
                JPushInterface.setAlias(this, "", mAliasCallback);
                SharePreCache.logOut();
                AppContext.getInstance().finishAllActivitys();
                AppContext.getInstance().cleanLoginInfo();
                SharedPreferencesUtils.putSharePrefBoolean("net_base", true);
                startActivity(LoginActivity.class);
                break;
        }
    }

    private void exit() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.e("MineFragment", "退出成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                LogUtil.d("MineFragment", "退出失败！");
            }
        });
    }

    private void changeEnv() {

        env_text.setText(CommonUrl.BASEURL);

        env_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long nextClickTime = SystemClock.uptimeMillis();
                if (lastClickTime <= 0) {
                    lastClickTime = SystemClock.uptimeMillis();
                    clickNum = 1;
                    return;
                } else {
                    if (nextClickTime - lastClickTime < 500) {
                        clickNum++;
                        switch (clickNum) {
                            case 10:
                                type = 4;
                                env_text.setText(CommonUrl.BASEURL_WWW);
                                break;
                            case 6:
                                type = 3;
                                env_text.setText(CommonUrl.BASEURL_DEV);
                                break;
                            case 4:
                                type = 2;
                                env_text.setText(CommonUrl.BASEURL_TEST1);
                                break;
                            case 2:
                                type = 1;
                                env_text.setText(CommonUrl.BASEURL_TEST);
                                break;
                        }
                    } else {
                        clickNum = 1;
                        lastClickTime = 0L;
                    }
                    lastClickTime = SystemClock.uptimeMillis();
                }
            }
        });

        env_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog.show();
                NetworkRequest.request(null, CommonUrl.logOut, Config.LOGOUT);
                return false;
            }
        });
    }


}
