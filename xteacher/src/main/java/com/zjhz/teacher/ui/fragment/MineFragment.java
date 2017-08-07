package com.zjhz.teacher.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CheckRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpdateInfo;
import com.zjhz.teacher.ui.activity.AboutWeActivity;
import com.zjhz.teacher.ui.activity.LoginActivity;
import com.zjhz.teacher.ui.activity.MyCenterActivity;
import com.zjhz.teacher.ui.activity.PayActivity;
import com.zjhz.teacher.ui.activity.UpdataPwdActivity;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 个人设置中心
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.mycenter_rl)
    RelativeLayout mycenterRl;
    @BindView(R.id.opinion_rl)
    RelativeLayout opinionRl;
    @BindView(R.id.custom_service_rl)
    RelativeLayout customServiceRl;
    @BindView(R.id.about_rl)
    RelativeLayout aboutRl;
    @BindView(R.id.updata_pwd)
    RelativeLayout updataPwd;
    @BindView(R.id.exit_login_rl)
    RelativeLayout exitLoginRl;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.newest_version_flag)
    TextView newVersionFlag;
    @BindView(R.id.mine_pay)
    RelativeLayout minePay;

    private PopupWindow popupWindow;
    private boolean isToastOpinion = true;

    private boolean isFirstVisible = true;
    UpdateInfo updateInfo = new UpdateInfo();

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        title_tv.setText("我的");
        title_back_img.setVisibility(View.GONE);

        if(isFirstVisible){
            isFirstVisible = false;
            NetworkRequest.request(new CheckRequest("1", "TEACHER_APP"), CommonUrl.NEWEST_VERSION, "newest_version");
        }
    }

    @OnClick({R.id.mycenter_rl,R.id.opinion_rl,R.id.custom_service_rl,R.id.about_rl,
            R.id.updata_pwd,R.id.exit_login_rl, R.id.mine_pay})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.mycenter_rl:
                startActivity(MyCenterActivity.class);
                break;
            case R.id.opinion_rl:
                if (isToastOpinion){
                    isToastOpinion = false;
                    ToastUtils.showShort("暂未开通");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                isToastOpinion = true;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
//                startActivity(OpinionActivity.class);
                break;
            case R.id.custom_service_rl:
                break;
            case R.id.about_rl:
                Bundle bundle = new Bundle();
                if(lowThanNewestVersion(BaseUtil.getPackageInfo(context).versionName, updateInfo.getVersionName()))
                    bundle.putSerializable("update_info",updateInfo);
                startActivity(AboutWeActivity.class, bundle);
                break;
            case R.id.updata_pwd:
                startActivity(UpdataPwdActivity.class);
                break;
            case R.id.exit_login_rl:
                if (popupWindow == null){
                    View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.hintpop,null);
                    popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
                    popupWindow.setOutsideTouchable(false);
                    view1.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            dialog.setMessage("正在退出...");
                            if (!dialog.isShowing()) {
                                dialog.show();
                            }

                            NetworkRequest.request(null, CommonUrl.logOut,Config.LOGOUT);
                        }
                    });
                    view1.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {popupWindow.dismiss();}
                    });
                }
                popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);
                break;
            case R.id.mine_pay:
                startActivity(PayActivity.class);
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

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {

        switch (eventCenter.getEventCode()){
            case Config.LOGOUT:
                dialog.dismiss();
                exit();
                JPushInterface.setAlias(getActivity(), "", mAliasCallback);
                SharePreCache.logOut();
                AppContext.getInstance().finishAllActivitys();
                AppContext.getInstance().cleanLoginInfo();
                SharedPreferencesUtils.putSharePrefBoolean("net_base",true);
                startActivity(LoginActivity.class);
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case Config.ERROR:
                dialog.dismiss();
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                JSONObject jsonObject = (JSONObject) eventCenter.getData();
                try {
                    if(jsonObject.getInt("code") == 1){
                        exit();
                        JPushInterface.setAlias(getActivity(),"",mAliasCallback);
                        SharePreCache.logOut();
                        AppContext.getInstance().finishAllActivitys();
                        SharedPreferencesUtils.putSharePrefBoolean("net_base",true);
                        startActivity(LoginActivity.class);
                    }else {
                        ToastUtils.showShort( jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "newest_version":
                try {
                    JSONObject jsonObject1 = (JSONObject) eventCenter.getData();
                    JSONObject data = jsonObject1.optJSONObject("data");
                    LogUtil.e("版本返回的data = ", data + "");
                    if (data != null) {
                        updateInfo.setApkUrl(data.optString("urlDownload"));
                        updateInfo.setVersionName(data.optString("appVersion"));
                        updateInfo.setChangeLog(data.optString("description"));
                        updateInfo.setAppName("千校云");

                        if (lowThanNewestVersion(BaseUtil.getPackageInfo(context).versionName, updateInfo.getVersionName())) {
                            newVersionFlag.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //低于最新版本
    private boolean lowThanNewestVersion(String currentVersion, String newestVersion) {
        if(currentVersion == null || newestVersion == null)
            return false;
        int length = Math.min(currentVersion.split("\\.").length, newestVersion.split("\\.").length);
        if (TextUtils.isEmpty(currentVersion) || TextUtils.isEmpty(newestVersion) || length == 0)
            return false;
        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(currentVersion.split("\\.")[i]) > Integer.parseInt(newestVersion.split("\\.")[i])) {
                return false;
            }else if(Integer.parseInt(currentVersion.split("\\.")[i]) == Integer.parseInt(newestVersion.split("\\.")[i])){
                continue;
            }else {
                return true;
            }
        }
        return false;
    }

    protected static final String TAG = MineFragment.class.getSimpleName();
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success"+alias;
                    Log.i(TAG, logs);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
}
