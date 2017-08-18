package xmvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zjhz.teacher.BuildConfig;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CenterParams;
import com.zjhz.teacher.NetworkRequests.request.UserLoginParams;
import com.zjhz.teacher.NetworkRequests.response.PersonInfoBean;
import com.zjhz.teacher.NetworkRequests.response.SchoolListBean;
import com.zjhz.teacher.NetworkRequests.response.UserLogin;
import com.zjhz.teacher.NetworkRequests.retrofit.Api;
import com.zjhz.teacher.NetworkRequests.retrofit.Model.LoginRespon;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.MainActivity;
import com.zjhz.teacher.ui.activity.SelectSchoolActivity;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.DeviceUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.net.ApiSubcriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import pro.kit.ViewTools;
import xmvp.base.BaseActivity;

/**
 * 登录
 * Created by xiangxue on 2016/6/1.
 * Modify fei.wang 2016/7/18
 */
public class LoginActivity extends BaseActivity {
    protected static final String TAG = LoginActivity.class.getSimpleName();
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
    @BindView(R.id.et_username)
    EditText mEtUserName;
    @BindView(R.id.et_userpassword)
    EditText mEtPassWord;
    @BindView(R.id.remember_pass_check_box)
    CheckBox mChkRememberPass;
    @BindView(R.id.auto_login_check_box)
    CheckBox mChkAutoLogin;
    @BindView(R.id.ll)
    LinearLayout ll;
    UserLogin userLogin;
    String baseUrl;
    private String mUserName = "";
    private String mPassword = "";
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mUserName = mEtUserName.getText().toString();
                mPassword = mEtPassWord.getText().toString();
                handleLogin();
            }
        }
    };
    private boolean isRememberMe = false;
    private boolean isAutoLogin = false;
    private String schoolId = "";
    private int SELECT_SCHOOL_REQUEST_CODE = 1;

    //    private HintPopwindow popwindow;
//    private String content = "";
//    private boolean isShow = true;
    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstantKey.isLogin = true;
        initView();
    }

    public void initView() {
        boolean isRemeber = SharedPreferencesUtils.getSharePrefBoolean(ConstantKey.isRemberPwdKey, false);
        mChkRememberPass.setChecked(isRemeber);
        isRememberMe = isRemeber;
        boolean isAuto = SharedPreferencesUtils.getSharePrefBoolean(ConstantKey.isAutoLogin, false);
        mChkAutoLogin.setChecked(isAuto);
        isAutoLogin = isAuto;
        initData();
    }

    public void initData() {
//        setSystemBarTintDrawable(getResources().getColor(R.color.title_background_red));
        //给两个输入框默认添加帐号和密码
        String userName = SharedPreferencesUtils.getSharePrefString(ConstantKey.AccountKey, "");
        if (!SharePreCache.isEmpty(userName)) {
            mEtUserName.setText(userName);
        }
        if (isRememberMe) {
            String passWord = SharedPreferencesUtils.getSharePrefString(ConstantKey.PwdKey, "");
            if (!SharePreCache.isEmpty(passWord)) {
                mEtPassWord.setText(passWord);
            }
        }

        if (BuildConfig.DEBUG) {
            String baseUrl = SharedPreferencesUtils.getSharePrefString(Config.BASE_URL);
            if (!TextUtils.isEmpty(baseUrl)) {
                CommonUrl.BASEURL = baseUrl;
                ToastUtils.showShort(CommonUrl.BASEURL);
            }
        }
    }

    @OnCheckedChanged(R.id.remember_pass_check_box)
    void onChecked(boolean checked) {
        mUserName = mEtUserName.getText().toString();
        mPassword = mEtPassWord.getText().toString();
        if (mUserName.equals("") && mPassword.equals("")) {
//            ToastUtils.showShort( Constants.FROM_ACCOUNT_INCOMPLETE);
            return;
        } else {
            isRememberMe = checked;
        }
    }

    @OnCheckedChanged(R.id.auto_login_check_box)
    void onCheck(boolean checked) {
        mUserName = mEtUserName.getText().toString();
        mPassword = mEtPassWord.getText().toString();
        if (mUserName.equals("") && mPassword.equals("")) {
//            ToastUtils.showShort(Constants.FROM_ACCOUNT_INCOMPLETE);
            return;
        } else {
            isAutoLogin = checked;
        }
    }

    @OnClick({R.id.bt_login})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.bt_login:
                //点击登录按钮
//                mUserName = mEtUserName.getText().toString();
//                mPassword = mEtPassWord.getText().toString();
//                handleLogin();
                if (!prepareForLogin()) {
                    check();
                }
                break;
            default:
                break;
        }
    }

    private boolean prepareForLogin() {
        mUserName = mEtUserName.getText().toString().trim();
        mPassword = mEtPassWord.getText().toString().trim();
        if (SharePreCache.isEmpty(mUserName)) {
            ToastUtils.showShort("输入账号/手机号");
            return true;
        }
        if (SharePreCache.isEmpty(mPassword)) {
            ToastUtils.showShort("请输入密码");
            return true;
        }
        if (mPassword.length() < 6) {
            ToastUtils.showShort("密码不能小于6位数");
            return true;
        }
        return false;
    }

    private void handleLogin() {
        BaseUtil.downKeyboard(mEtUserName);
        BaseUtil.downKeyboard(mEtPassWord);
//        showWaitDialog("正在登录…");
        LogUtil.e("登陆请求参数 = ", GsonUtils.toJson(new UserLoginParams(mUserName, mPassword, "APP", "teacher", DeviceUtil.getIMEI())));
        NetworkRequest.request(new UserLoginParams(mUserName, mPassword, "APP", "teacher", DeviceUtil.getIMEI()), CommonUrl.login, Config.LOGIN);
        Map<String, String> map = new HashMap<>();
        map.put("userName", mUserName);
        map.put("pwd", mPassword);
        map.put("os", "APP");
        map.put("appType", "teacher");
        map.put("imei", DeviceUtil.getIMEI());
        Api.getLoginService().login(CommonUrl.login, map)
                .compose(XApi.<LoginRespon>getApiTransformer())
                .compose(XApi.<LoginRespon>getScheduler())
//                .compose(.<GankResults>bindToLifecycle())
                .subscribe(new ApiSubcriber<LoginRespon>() {
                    @Override
                    protected void onFail(NetError error) {
                        ToastUtils.showShort("failed");
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(LoginRespon loginRespon) {
                        userLogin = loginRespon.getData();
                        userLogin.setAutoLogin(isAutoLogin);
                        userLogin.setRemberPwd(isRememberMe);
                        userLogin.setAccount(mUserName);
                        userLogin.setPwd(mPassword);
                        LogUtil.i("LoginActivity", "token = " + userLogin.getToken());
                        SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, userLogin.getToken());
                        SharedPreferencesUtils.putSharePrefString("login_userid", userLogin.getUserId());
                        SharedPreferencesUtils.putSharePrefString("app_login_teacherId", userLogin.getTeacherId());
                        AppContext.getInstance().saveUserInfo();
//                            SharedPreferencesUtils.putSharePrefString("app_login_schoolId",userLogin.getSchoolId());
//                            SharedPreferencesUtils.putSharePrefString("app_login_head",userLogin.getPhotoUrl());
//                            SharedPreferencesUtils.putSharePrefString("app_login_name",userLogin.getNickName());
                        //获取个人信息
                        NetworkRequest.request(new CenterParams(userLogin.getTeacherId()), CommonUrl.PersonInfo, Config.PersonInfo);
                        hxLogin(userLogin.getUserId(), "e10adc3949ba59abbe56e057f20f883e");
                    }
                });
//        NetworkRequest.request(new UserLoginParams(mUserName, mPassword, "app", schoolId), CommonUrl.login, Config.LOGIN);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("网络请求失败");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                JSONObject objectNo = (JSONObject) event.getData();
                try {
                    LogUtil.e(objectNo.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Config.LOGIN:
                JSONObject object = (JSONObject) event.getData();
                try {
                    if (object.getInt("code") == 1009) {
                        List<SchoolListBean> beanList = GsonUtils.toArray(SchoolListBean.class, object);
                        if (beanList != null && beanList.size() > 0) {
                            SchoolListBean bean = new SchoolListBean();
                            bean.setBeen(beanList);
                            Intent intent = new Intent(this, SelectSchoolActivity.class);
                            intent.putExtra("bean", bean);
                            startActivityForResult(intent, SELECT_SCHOOL_REQUEST_CODE);
                            dialog.dismiss();
                        } else {
                            ToastUtils.showShort("登录失败,请重新登录");
                            dialog.dismiss();
                        }
                    } else {
                        userLogin = GsonUtils.toObjetJson(object, UserLogin.class);
                        if (userLogin != null) {
                            userLogin.setAutoLogin(isAutoLogin);
                            userLogin.setRemberPwd(isRememberMe);
                            userLogin.setAccount(mUserName);
                            userLogin.setPwd(mPassword);
                            LogUtil.i("LoginActivity", "token = " + userLogin.getToken());
                            SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, userLogin.getToken());
                            SharedPreferencesUtils.putSharePrefString("login_userid", userLogin.getUserId());
                            SharedPreferencesUtils.putSharePrefString("app_login_teacherId", userLogin.getTeacherId());
                            AppContext.getInstance().saveUserInfo();
//                            SharedPreferencesUtils.putSharePrefString("app_login_schoolId",userLogin.getSchoolId());
//                            SharedPreferencesUtils.putSharePrefString("app_login_head",userLogin.getPhotoUrl());
//                            SharedPreferencesUtils.putSharePrefString("app_login_name",userLogin.getNickName());
                            //获取个人信息
                            NetworkRequest.request(new CenterParams(userLogin.getTeacherId()), CommonUrl.PersonInfo, Config.PersonInfo);
                            hxLogin(userLogin.getUserId(), "e10adc3949ba59abbe56e057f20f883e");

                        } else {
                            ToastUtils.showShort("登录失败,请重新登录");
                            dialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showShort("登录失败，请重新登录");
                    dialog.dismiss();
                }
                break;
            case Config.PersonInfo:
                JSONObject js = (JSONObject) event.getData();
                PersonInfoBean b = GsonUtils.toObjetJson(js, PersonInfoBean.class);
                if (b != null) {
                    SharedPreferencesUtils.putSharePrefString(ConstantKey.teacherSexKey, b.getSexVal());
                    SharedPreferencesUtils.putSharePrefString(ConstantKey.teacherDeptKey, b.getDeptName());
                    SharedPreferencesUtils.putSharePrefString(ConstantKey.jobNumberKey, b.getJobNumber());
                }
                loginSuccess();
                break;
        }
    }

    private void hxLogin(String userName, String password) {
        LogUtil.e("环信", "userName = " + userName + "  " + "password = " + password);
        EMClient.getInstance().login(userName, password, new EMCallBack() { //回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                dialog.dismiss();
                LogUtil.e("LoginActivity", "登录成功！");
                SharedPreferencesUtils.putSharePrefString("fly_head_photo_hx", userLogin.getPhotoUrl());
                SharedPreferencesUtils.putSharePrefString("fly_nick_name_hx", userLogin.getNickName());
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                dialog.dismiss();
                LogUtil.e("LoginActivity", "环信登录失败！" + message + " code = " + code);
            }
        });
    }
//    private void toSelectSchool() {
//        Intent intent = new Intent(LoginActivity.this,ReceiveSelectActivity.class);
//        startActivityForResult(intent,SELECT_SCHOOL_REQUEST_CODE);
//    }

    private void loginSuccess() {
        dialog.dismiss();
        SharePreCache.login(userLogin);
        JPushInterface.setAlias(this, userLogin.getUserId(), mAliasCallback);
        Router.newIntent(this).to(MainActivity.class).launch();
        ConstantKey.isLogin = false;
    }

    // String[] strArray={"eduxiaoyuan.com"};               //Test
//   String[] strArray={"www.1000xyun.com"};                    //Pro
    // String[] strArray={"k12xiaoyuan.com"};                   //Pro
    // String[] strArray={"k12xiaoyuan.com"};                   //Emu

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == SELECT_SCHOOL_REQUEST_CODE) {
            if (data != null) {
//                login_school_name.setText(data.getStringExtra("schoolName"));
                schoolId = data.getStringExtra("schoolId");
                showWaitDialog("正在登录…");
                NetworkRequest.request(new UserLoginParams(mUserName, mPassword, "app", schoolId), CommonUrl.login, Config.LOGIN);
            }
        }
    }

    private void check() {
        if (prepareForLogin()) {
            return;
        }
        showWaitDialog("正在登录…");
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = Message.obtain();
                SharedPreferencesUtils.putSharePrefBoolean("net_base", true);
                if (SharedPreferencesUtils.getSharePrefBoolean("net_base", true)) {
                    SharedPreferencesUtils.putSharePrefBoolean("net_base", false);
                    LogUtil.e("NetworkRequest boolean 里面 = ", SharedPreferencesUtils.getSharePrefBoolean("net_base", true) + "");
//                    for (int i = 0; i < strArray.length; i++) {
//                        Process p1;
//                        try {
//                            p1 = Runtime.getRuntime().exec("ping -c 1 -w 100 " + strArray[i]);
//                            int status1 = p1.waitFor();
//                            if (status1 == 0) {
//                                baseUrl = strArray[i];
//                                msg.what = 0;
//                                break;
//                            } else {
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
//                else{
//            SharedPreferencesUtils.putSharePrefBoolean("net_base",true);
//            LogUtil.e("NetworkRequest boolean    else里面 = ",SharedPreferencesUtils.getSharePrefBoolean("net_base",true) + "");
//                }

//                SharedPreferencesUtils.putSharePrefString("base_url", AppContext.getInstance().domainName.get(baseUrl));
//                LogUtil.e("BaseUrl = ", AppContext.getInstance().domainName.get(baseUrl) + "");
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //切换账号需要清除之前的班级信息
        AppContext.getInstance().gradeBeans.clear();
        AppContext.getInstance().clazzs.clear();
        AppContext.getInstance().grades.clear();
    }


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public Object newP() {
        return null;
    }


}
