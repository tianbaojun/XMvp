package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pro.kit.ViewTools;

public class TeacherDutyAddActivity extends BaseActivity {

    @BindView(R.id.right_text)
    TextView submit;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.text_input)
    EditText text_input;
    @BindView(R.id.bottom_rl)
    RelativeLayout bottom_rl;

    private String inputText;
    private String dutyId;
    private String dutyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_duty_add);
        setMyTitle("教师值日");
        submit.setVisibility(View.VISIBLE);
        bottom_rl.setVisibility(View.GONE);
        submit.setText(R.string.submit);
        dutyId = getIntent().getStringExtra("dutyId");
        dutyTime = getIntent().getStringExtra("dutyTime");
        time_tv.setText(dutyTime);
    }

    @OnClick({R.id.right_text})
    public void OnClick(View view) {
        if(ViewTools.avoidRepeatClick(view)){
            return;
        }
        switch (view.getId()){
            case R.id.right_text:
                inputText = text_input.getText().toString();
                if(SharePreCache.isEmpty(inputText)){
                    ToastUtils.showShort("请输入内容");
                }else{
                    Map<String,String> map = new HashMap<>();
                    map.put("dutyId",dutyId);
                    map.put("dutyTime",dutyTime);
                    map.put("content",inputText);
                    map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
                    NetworkRequest.request(map, CommonUrl.ADDDUTYAFFAIRS, Config.ADDDUTYAFFAIRS);
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter center){
        switch (center.getEventCode()) {
            case Config.NOSUCCESS:
                ToastUtils.showShort("提交失败");
                break;
            case Config.ADDDUTYAFFAIRS:
                ToastUtils.showShort("提交成功");
                finish();
                break;
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
}
