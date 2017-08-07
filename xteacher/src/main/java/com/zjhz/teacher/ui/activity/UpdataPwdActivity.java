package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ChangePwdParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 * Created by 向雪 on 2016/6/2.
 * Modify: fei.wang on 2016/8/20.
 */
public class UpdataPwdActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.et_old_pwd)
    EditText et_old_pwd;
    @BindView(R.id.et_new_pwd)
    EditText et_new_pwd;
    @BindView(R.id.et_again_pwd)
    EditText et_again_pwd;
    private String oldpwd;
    private String newpwd;
    private String againpwd;
    private final static String TAG = UpdataPwdActivity.class.getSimpleName();
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
    }
    public void initView() {
        title_tv.setText("修改密码");
        title_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.commit_btn)
    public void clickEvent(View view) {
        switch (view.getId()){
            case R.id.commit_btn:
                toCommit();
                break;
        }
    }

    private void toCommit() {
        String userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
        if (SharePreCache.isEmpty(userId)){
            ToastUtils.showShort("用户ID错误");
            return;
        }
        if (isEmpty()){
            return;
        }

        LogUtil.e("oldpwd = " + oldpwd);
        LogUtil.e("newpwd = " + newpwd);
        LogUtil.e("againpwd = " + againpwd);
        if (oldpwd.contains(" ") || newpwd.contains(" ") || againpwd.contains(" ")) {
            ToastUtils.showShort("输入的密码中有空格，请重新输入");
            return;
        }

        dialog.setMessage("正在修改...");
        dialog.show();
        NetworkRequest.request(new ChangePwdParams(oldpwd,newpwd,againpwd,userId), CommonUrl.updataPwd,"changePwd");
    }

    @Subscribe
    public void onEventMainThread(EventCenter event){
        switch (event.getEventCode()){
            case Config.ERROR :
                dialog.dismiss();
                ToastUtils.showShort("网络请求失败");
                break;
            case Config.NOSUCCESS :
                dialog.dismiss();
                ToastUtils.showShort("修改失败");
                break;
            case "changePwd" :
                dialog.dismiss();
                ToastUtils.showShort("修改成功");
                finish();
                break;
        }
    }

    private boolean isEmpty() {
        oldpwd = et_old_pwd.getText().toString();
        newpwd = et_new_pwd.getText().toString();
        againpwd = et_again_pwd.getText().toString();
        if (SharePreCache.isEmpty(oldpwd)){
            ToastUtils.showShort("请输入旧密码");
            return true;
        }
        if (SharePreCache.isEmpty(newpwd)){
           ToastUtils.showShort("请输入新密码");
            return true;
        }
        if (SharePreCache.isEmpty(againpwd)){
            ToastUtils.showShort("再次输入新密码");
            return true;
        }
        if (newpwd.length() < 6 || againpwd.length() < 6 || oldpwd.length() < 6){
            ToastUtils.showShort("密码不能小于6位数");
            return true;
        }
        if (!newpwd.equals(againpwd)){
            ToastUtils.showShort("两次新密码不同");
            return true;
        }
        if (oldpwd.equals(againpwd)){
            ToastUtils.showShort("旧密码与新密码相同");
            return true;
        }
        return false;
    }
}
