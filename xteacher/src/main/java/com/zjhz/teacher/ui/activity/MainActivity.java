package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.UnreadMsgParams;
import com.zjhz.teacher.NetworkRequests.request.VersionInfoRequest;
import com.zjhz.teacher.NetworkRequests.response.ErrorUtils;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Classz;
import com.zjhz.teacher.bean.DownloadBean;
import com.zjhz.teacher.bean.Grade;
import com.zjhz.teacher.bean.UpdateInfo;
import com.zjhz.teacher.hx.domain.EaseUser;
import com.zjhz.teacher.hx.ui.EaseConversationListFragment;
import com.zjhz.teacher.ui.fragment.ExChangeFragment;
import com.zjhz.teacher.ui.fragment.MineFragment;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.DownLoadAsyncTask;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.OnUpdateListener;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.UpdateHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;
import pro.ui.fragment.HomeFragment;
import pro.ui.fragment.MessageListFragment;import butterknife.BindView;

import static com.zjhz.teacher.utils.GsonUtils.toArray;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: MainActivity
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.unread_msg_number1)
    public TextView unread_msg_number1;
    @BindView(R.id.unread_msg_number)
    public TextView unreadLabel;
    @BindView(R.id.tab_btn_home)
    Button tabBtnHome;
    @BindView(R.id.tab_btn_message)
    Button tabBtnMessage;
    @BindView(R.id.tab_btn_mine)
    Button tabBtnMine;
    @BindView(R.id.tab_btn_exchange)
    Button tabBtnExchange;
    private long exitTime = 0;
    private TextView[] mTabs;
    private HomeFragment homeFragment;
    //    private MessageFragment messageFragment;
    private MessageListFragment messageFragment;
    private ExChangeFragment exChangeFragment;
    private MineFragment mineFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private EaseConversationListFragment conversationListFragment;
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                AppContext.getInstance().getNotifier().onNewMsg(message);
                AppContext.getInstance().messageHxs.add(message);
                String userPic = message.getStringAttribute("head", "");
                String userName = message.getStringAttribute("name", "");
                String hxIdFrom = message.getFrom();
                EaseUser easeUser = new EaseUser(hxIdFrom);
                easeUser.setAvatar(userPic);
                easeUser.setNick(userName);
                AppContext.getInstance().fly.put(hxIdFrom, easeUser);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };
    private PackageInfo packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.title_background_red));
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ConstantKey.isForeground = true;
        initView();
        conversationListFragment = new EaseConversationListFragment();
        AppContext.getInstance().addActivity(TAG, MainActivity.this);
        getUnReadMsgNums();
//        setSystemBarTintDrawable("#24b7a4");
        getClassz();
        checkUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            ToastUtils.showShort("为保证程序正常运行，请重启并安装最低支持版本");
        }
    }

    private void checkUpdate() {
        UpdateHelper updateHelper = new UpdateHelper.Builder(this)
                .checkUrl(CommonUrl.LOWEST_VERSION)
                .isAutoInstall(true)
                .build();
        updateHelper.check(new OnUpdateListener() {
            @Override
            public void onStartCheck() {

            }

            @Override
            public void onFinishCheck(UpdateInfo info, boolean isUpdateLowestVersion) {

                if (!isUpdateLowestVersion &&
                        !SharedPreferencesUtils.getSharePrefString("patch", "")
                                .equals(BaseUtil.getPackageInfo(MainActivity.this).versionName)) {
                    getPatch();
                    LogUtil.i("下载插件");
                }
            }

            @Override
            public void onStartDownload() {

            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onFinshDownload() {

            }
        });
    }

    private void getPatch() {
        packageInfo = BaseUtil.getPackageInfo(this);
        String appVersion = packageInfo.versionName;
        NetworkRequest.request(new VersionInfoRequest("1", "TEACHER_APP", appVersion), CommonUrl.VERSION_INFO, "version_info");
    }

    private void getUnReadMsgNums() {
        NetworkRequest.request(new UnreadMsgParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey), "1,2,3,4,7", "0"), CommonUrl.CHECKUNREADNUM, "unReadNum");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (eventCenter.getEventCode().equals("unReadNum")) {
            JSONObject jsonObject = (JSONObject) eventCenter.getData();
            try {
                int total = jsonObject.getInt("data");
                if (total > 0) {
                    unread_msg_number1.setText(total + "");
                    unread_msg_number1.setVisibility(View.VISIBLE);
                } else {
                    unread_msg_number1.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                unread_msg_number1.setVisibility(View.INVISIBLE);
            }
        } else if (eventCenter.getEventCode().equals("updateDataNum")) {
            if (messageFragment.getUserVisibleHint()) {
                messageFragment.getData();
            }
            getUnReadMsgNums();
        } else if (eventCenter.getEventCode().equals("version_info")) {
            String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            String appVersion = packageInfo.versionName;
            JSONObject jsonObject = (JSONObject) eventCenter.getData();
            UpdateInfo updateInfo = new UpdateInfo();
            JSONObject data = jsonObject.optJSONObject("data");
            if (data != null && !"{}".equals(data.toString())) {
                try {
                    updateInfo.setPatchUrl(data.getString("patchUrl"));
                    updateInfo.setAppName(appName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(updateInfo.getPatchUrl())) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.url = updateInfo.getPatchUrl();
                downloadBean.fileName = appName + appVersion + ".patch"; //这里暂时只支持一个差量包
                DownLoadAsyncTask asyncDownLoad = new DownLoadAsyncTask(this, new DownLoadAsyncTask.DownLoadFileListener() {
                    @Override
                    public void onDownLoad(int progress) {

                    }

                    @Override
                    public void onDownLoadErr() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        ToastUtils.showShort("下载失败");

                    }

                    @Override
                    public void completeDownLoad(String savePathName, String fileName) {
                        LogUtil.i("安装插件");
                        TinkerInstaller.onReceiveUpgradePatch(MainActivity.this, savePathName + File.separator + fileName);
                    }
                });
                asyncDownLoad.execute(downloadBean);
            } else {
                //没有差量包
            }
        }
        if (eventCenter.getEventCode().equals(Config.gradeClsList)) {//获取年级信息   添加到appcontext
            List<Grade> grades = toArray(Grade.class, (JSONObject) eventCenter.getData());
            if (null == grades || grades.size() == 0) {
                ToastUtils.showShort(R.string.grade_data_not_found);
            } else {
                ArrayList<ArrayList<String>> gradeClassNames = new ArrayList<>();
                ArrayList<String> gradeNames = new ArrayList<>();
                if (grades != null && !grades.isEmpty()) {
                    for (Grade grade : grades) {
                        gradeNames.add(grade.getName());
                        List<Classz> classzs = grade.getDetail();
                        ArrayList<String> classNames = new ArrayList<>();
                        if (classzs != null && classzs.size() > 0) {
                            for (Classz classz : classzs) {
                                classNames.add(classz.getName());
                            }
                        }
                        gradeClassNames.add(classNames);
                    }
                }
                AppContext.getInstance().grades.clear();
                AppContext.getInstance().clazzs.clear();
                AppContext.getInstance().gradeBeans.clear();

                AppContext.getInstance().grades.addAll(gradeNames);
                AppContext.getInstance().clazzs.addAll(gradeClassNames);
                AppContext.getInstance().gradeBeans.addAll(grades);
                Log.e("main", grades.toString() + "\n" + gradeClassNames.toString());
            }
        }
    }

    private void initView() {
//        沉浸式状态栏
//        setSystemBarTintDrawable(getResources().getColor(R.color.title_background_red));
        mTabs = new TextView[4];
        mTabs[0] = tabBtnHome;
        mTabs[1] = tabBtnMessage;
        mTabs[2] = tabBtnExchange;
        mTabs[3] = tabBtnMine;
        mTabs[0].setSelected(true);
        homeFragment = new HomeFragment();
        messageFragment = new MessageListFragment();
        exChangeFragment = new ExChangeFragment();
        mineFragment = new MineFragment();
        fragments = new Fragment[]{homeFragment, messageFragment, exChangeFragment, mineFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, homeFragment).show(homeFragment).commit();
    }

    @OnClick({R.id.tab_btn_home, R.id.tab_btn_message, R.id.tab_btn_mine, R.id.tab_btn_exchange})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.tab_btn_home:
                index = 0;
                break;
            case R.id.tab_btn_message:
                index = 1;
                break;
            case R.id.tab_btn_exchange:
                AppContext.getInstance().messageHxs.clear();
                unreadLabel.setVisibility(View.GONE);
                index = 2;
                break;
            case R.id.tab_btn_mine:
                index = 3;
                break;
        }
        disPlay(index);
    }

    private void disPlay(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                fragmentTransaction.add(R.id.main_frame, fragments[index]);
            }
            fragmentTransaction.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", index);
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int savedIndex = savedInstanceState.getInt("index");
        if (savedIndex != this.index) {
            if (homeFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
            }
            if (savedIndex == 1) {
                disPlay(1);
            } else if (savedIndex == 2) {
                disPlay(2);
            } else if (savedIndex == 3) {
                disPlay(3);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppContext.getInstance().finishAllActivitys();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (index != 2) {
                    unreadLabel.setVisibility(View.VISIBLE);
                }
                EventBus.getDefault().post(new EventCenter<Object>(Config.LOOK, new ErrorUtils("vacant")));
                if (AppContext.getInstance().messageHxs.size() > 99) {
                    unreadLabel.setText("99");
                } else {
                    unreadLabel.setText(AppContext.getInstance().messageHxs.size() + "");
                }
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
            }
        });
    }

    public void hasReadMsg() {
        unread_msg_number1.setText("");
        unread_msg_number1.setVisibility(View.INVISIBLE);
    }

    /**
     * 获取班级列表
     */
    public static void getClassz() {
        NetworkRequest.request(null, CommonUrl.gradeClsList, Config.gradeClsList);
    }

}
