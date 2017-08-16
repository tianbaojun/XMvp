package xmvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.ui.activity.GuideActivity;
import com.zjhz.teacher.ui.activity.LoginActivity;
import com.zjhz.teacher.ui.activity.MainActivity;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by xiangxue on 2016/6/7.
 * Modify: fei.wang on 2016/8/10.
 */
public class AppStartActivity extends BaseActivity {

    @BindView(R.id.login_version)
    TextView loginVersion;
    private final static String TAG = AppStartActivity.class.getSimpleName();

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
    private AlphaAnimation start_anima;
    //    String[] strArray={"eduyunxiao.com","eduxiaoyuan.com","51jxh.com","k12yunxiao.com","k12xiaoyuan.com"};
    private View view;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_appstart, null);
        setContentView(view);
        SharedPreferencesUtils.putSharePrefString(ConstantKey.lastStuNameKey, "选择学生");
        setVersionText();
        initData();
    }

    //设置版本号  年份为当前年
    private void setVersionText() {
        //版本号
        SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
        String date=df.format(new Date());
        String versionF = getResources().getString(R.string.sys_version);
        String version = String.format(versionF, date);
        loginVersion.setText(version);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantKey.isForeground = true;
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConstantKey.isForeground = false;
        JPushInterface.onPause(this);
    }

    private void initData() {
        start_anima = new AlphaAnimation(0.3f, 1.0f);
        start_anima.setDuration(2000);
        view.startAnimation(start_anima);
        start_anima.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if (SharedPreferencesUtils.getSharePrefBoolean(ConstantKey.isFrist,true)){
                    JPushInterface.setAlias(AppStartActivity.this,"",mAliasCallback);
                    startActivityThenKill(GuideActivity.class);
                }else {
                    if (SharePreCache.isEmpty(SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey))){
                        JPushInterface.setAlias(AppStartActivity.this,"",mAliasCallback);
                        startActivityThenKill(LoginActivity.class);
                    }else {
                        startActivityThenKill(MainActivity.class);
                    }
                }
            }
        });
    }
}
