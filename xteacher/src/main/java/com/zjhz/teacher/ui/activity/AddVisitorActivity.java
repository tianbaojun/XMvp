package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.VisitorBean;
import com.zjhz.teacher.databinding.ActivityAddVisitorBinding;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddVisitorActivity extends BaseActivity {
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.visit_name_ed)
    EditText nameEd;
    @BindView(R.id.visit_phone_ed)
    EditText phoneEd;
    @BindView(R.id.visit_num_ed)
    EditText numEd;
    @BindView(R.id.visit_time_tv)
    TextView timeTv;
    @BindView(R.id.visit_company_ed)
    EditText companyEd;
    @BindView(R.id.visit_reason_ed)
    EditText reasonEd;
    @BindView(R.id.send_verify_code)
    TextView sendCodeTextView;
    @BindView(R.id.remarks)
    TextView remarksTv;
    @BindView(R.id.title_back_img)
    TextView back_icon;

    private int cutDownTime = 120;
    private int count = 0;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            cutDownTime--;
            if (cutDownTime > 0)
                sendCodeTextView.setText("120s后重试:  " + cutDownTime + "s");
            if (cutDownTime == 0 && count < 3) {
                sendCodeTextView.setText(getResources().getString(R.string.send_verify_code));
                sendCodeTextView.setClickable(true);
                count++;
            } else if (count >= 3) {
                sendCodeTextView.setText("已发3次");
            }
        }
    };
    private String recordId;
    private VisitorBean visitorBean = new VisitorBean();
    private Thread thread;
    private boolean isSendCOde = false;
    private boolean isSave = false;
    private boolean isCG = false;//是否草稿

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            visitorBean = (VisitorBean) getIntent().getExtras().getSerializable("bean");
            recordId = visitorBean != null ? visitorBean.recordId : null;
            isCG = true;
        }

        ActivityAddVisitorBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_visitor);
        binding.setVisitor(visitorBean);
        ButterKnife.bind(this);

        titleTv.setText("添加访客");
        rightText.setVisibility(View.VISIBLE);
        if (visitorBean.visitStatus == 0) {//草稿状态
            rightText.setText("提交");
        } else {
            rightText.setText("保存");
            sendCodeTextView.setVisibility(View.GONE);
            remarksTv.setVisibility(View.GONE);
        }


    }

    @OnClick({R.id.right_text, R.id.send_verify_code, R.id.visit_time_tv, R.id.title_back_img})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.right_text:
                if ("保存".equals(rightText.getText().toString())) {
                    if (checkEdNotEmpty()) {
                        //TODO 保存
                        visitorBean.teacherId = SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("teacherId", visitorBean.teacherId);
                        map.put("visitorName", visitorBean.visitorName);
                        map.put("visitorPhone", visitorBean.visitorPhone);
                        map.put("visitorNum", visitorBean.visitorNum);
                        map.put("appointmentTime", visitorBean.appointmentTime);
                        map.put("visitorOrg", visitorBean.visitorOrg);
                        map.put("reason", visitorBean.reason);
                        map.put("visitStatus", "0");
                        NetworkRequest.request(map, CommonUrl.VISITOR_ADD, "visitor_add");
                        dialog.show();
                    }
                } else if ("提交".equals(rightText.getText().toString())) {
                    if (isSendCOde) {
                        if (isCG) {
                            NetworkRequest.request(visitorBean, CommonUrl.VISITOR_SUBMIT, "visitor_submit");
                        } else {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("recordId", recordId);
                            NetworkRequest.request(map, CommonUrl.VISITOR_SUBMIT, "visitor_submit");
                        }
                        dialog.show();
                    } else
                        ToastUtils.showShort("验证码还未发送成功");
                }
                break;
            case R.id.send_verify_code:
                //TODO 发送验证码
                sendVerifyCode();
                cutDownTime = 120;
                thread = new Thread(new MyRunnable());
                thread.start();
                sendCodeTextView.setClickable(false);
                break;
            case R.id.visit_time_tv:
                TimeUtil.selectDateYMDHM(this, timeTv);
                break;
            case R.id.title_back_img:
                if (isSave)
                    backSave();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isSave)
            backSave();
    }

    private boolean checkEdNotEmpty() {
        if (TextUtils.isEmpty(nameEd.getText().toString())) {
            ToastUtils.showShort("访客姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(phoneEd.getText().toString())) {
            ToastUtils.showShort("访客手机号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(numEd.getText().toString())) {
            ToastUtils.showShort("同行人数不能为空");
            return false;
        }
        if (TextUtils.isEmpty(timeTv.getText().toString())) {
            ToastUtils.showShort("访问时间不能为空");
            return false;
        }
        return true;
    }

    private void sendVerifyCode() {
        HashMap<String, String> map = new HashMap<>();
        map.put("recordId", recordId);
        NetworkRequest.request(map, CommonUrl.VISITOR_SEND_CODE, "send_code");

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "visitor_add":
                JSONObject jsonObject = (JSONObject) event.getData();
                try {
                    recordId = jsonObject.getJSONObject("data").getString("recordId");
                    if (recordId != null) {
                        sendCodeTextView.setVisibility(View.VISIBLE);
                        remarksTv.setVisibility(View.VISIBLE);
                        rightText.setText("提交");
                        visitorBean.visitStatus = 0;
                        visitorBean.recordId = recordId;
                        isSave = true;
                        ToastUtils.showShort("保存成功，请给家长发送验证码");
                        lockView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "send_code":
                JSONObject jsonObject1 = (JSONObject) event.getData();
                try {
                    String msg = jsonObject1.getString("msg");
                    if ("发送成功".equals(msg)) {
                        isSendCOde = true;
                        visitorBean.visitStatus = 1;
                        ToastUtils.showShort("发送成功，请提交数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                JSONObject jsonObject = (JSONObject) event.getData();
                break;
            case "visitor_submit":
                try {
                    String isValidate = ((JSONObject) event.getData()).getJSONObject("data").optString("isValidate");
                    if ("success".equals(isValidate)) {
                        ToastUtils.showShort("提交成功");
                        visitorBean.visitStatus = 1;
                        backSave();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //锁定页面，不可编辑
    private void lockView() {
        nameEd.setEnabled(false);
        phoneEd.setEnabled(false);
        numEd.setEnabled(false);
        timeTv.setClickable(false);
        companyEd.setEnabled(false);
        reasonEd.setEnabled(false);
    }

    private void backSave() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", visitorBean);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    public class MyRunnable implements Runnable {      // thread
        @Override
        public void run() {
            while (cutDownTime > 0) {
                try {
                    Thread.sleep(1000);     // sleep 1000ms
                    Message message = new Message();
                    handler.sendMessage(message);
                } catch (Exception e) {
                }
            }
        }
    }
}
