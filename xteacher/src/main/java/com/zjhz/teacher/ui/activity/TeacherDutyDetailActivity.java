package com.zjhz.teacher.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
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
import com.zjhz.teacher.bean.DutyAffairs;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pro.kit.ViewTools;

import static com.zjhz.teacher.NetworkRequests.CommonUrl.MODIFYAFFAIRS;
import static com.zjhz.teacher.NetworkRequests.CommonUrl.REMOVEAFFARIS;

public class TeacherDutyDetailActivity extends BaseActivity {

    @BindView(R.id.text_input)
    EditText text_input;
    @BindView(R.id.re_submit)
    Button re_submit;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.bottom_rl)
    RelativeLayout bottom_rl;


    private String time;
    private String affairId;
    private String inputText;
    private List<DutyAffairs> beans = new ArrayList<>();

    private boolean isModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_duty_add);
        Intent intent = getIntent();
        if(intent!=null){
            time = intent.getStringExtra("dutyTime");
            time_tv.setText(time);
        }
        setMyTitle("值日管理");
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("dutyTime", time);
        NetworkRequest.request(map, CommonUrl.AffairList, "AffairList");
    }

    @OnClick({R.id.re_submit, R.id.delete})
    public void OnClick(View view) {
        switch (view.getId()){
            case R.id.re_submit:
                inputText = text_input.getText().toString();
                if(SharePreCache.isEmpty(inputText)){
                    ToastUtils.showShort("请输入内容");
                }else {
                    reSubmit();
                }
                break;
            case R.id.delete:
                delete();
                break;
        }
    }

    private void reSubmit() {
        Map<String,String> map = new HashMap<>();
        map.put("affairId",beans.get(0).getAffairId());
        map.put("content",inputText);
        NetworkRequest.request(map, MODIFYAFFAIRS, "MODIFYAFFAIRS");
        showWaitDialog("提交中");
    }
    private void delete() {
        ViewTools.showDialog(TeacherDutyDetailActivity.this, "确定删除？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> map = new HashMap<>();
                        map.put("affairId", beans.get(0).getAffairId());
                        NetworkRequest.request(map, REMOVEAFFARIS, "REMOVEAFFARIS");
                        TeacherDutyDetailActivity.this.showWaitDialog("加载中");
                    }
                });
    }

    @Subscribe
    public void onEventMainThread(EventCenter center) {
        switch (center.getEventCode()) {
            case "AffairList":
                dialog.dismiss();
                beans.clear();
                beans.addAll(GsonUtils.toArray(DutyAffairs.class, (JSONObject) center.getData()));
                if (!BaseUtil.isEmpty(beans)) {
                    text_input.setText(beans.get(0).getContent());
                    if(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey).equals(beans.get(0).getTeacherId())){
                        bottom_rl.setVisibility(View.VISIBLE);
                        text_input.setInputType(InputType.TYPE_CLASS_TEXT);
                    }else{
                        bottom_rl.setVisibility(View.GONE);
                        text_input.setInputType(InputType.TYPE_NULL);
                    }
                }
                break;
            case "MODIFYAFFAIRS":
                ToastUtils.showShort("提交成功");
                finish();
                break;
            case "REMOVEAFFARIS":
                ToastUtils.showShort("删除成功");
                finish();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                String str = ((JSONObject) center.getData()).optString(NetworkRequest.FROMCODE);
                if (str.equals(REMOVEAFFARIS)) {
                    ToastUtils.showShort("删除失败");
                }
                if (str.equals("MODIFYAFFAIRS")) {
                    ToastUtils.showShort("提交失败");
                }
                break;
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }


}
