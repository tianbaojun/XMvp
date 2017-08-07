package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.VisitorBean;
import com.zjhz.teacher.databinding.ActivityVisitorManagerGuardBinding;
import com.zjhz.teacher.ui.view.zxing.MyCaptureActivity;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisitorManagerGuardActivity extends BaseActivity {

    private final static int SCAN_IN = 1;
    private final static int SCAN_OUT = 2;
    private final static int IN = 1;
    private final static int OUT = 2;
    @BindView(R.id.title_back_img)
    TextView back_icon;
    @BindView(R.id.title_tv)
    TextView title;
    @BindView(R.id.scan_in)
    ImageView scanIn;
    @BindView(R.id.scan_out)
    ImageView scanOut;
    @BindView(R.id.scan_in_tv)
    TextView scanInTextView;
    @BindView(R.id.scan_out_tv)
    TextView scanOutTextView;
    @BindView(R.id.verify_code_ed)
    TextView verifyCodeEd;
    @BindView(R.id.verify_code)
    TextView verifyCodeTv;
    //    @BindView(R.id.visit_really_time_text)
//    TextView reallyInTimeTv;
//    @BindView(R.id.visit_out_time_text)
//    TextView outTimeTv;
    @BindView(R.id.status_complete)
    LinearLayout statusComplete;
    @BindView(R.id.status_scan)
    LinearLayout statusScan;
    ActivityVisitorManagerGuardBinding binding;
    private VisitorBean visitorBean;
    private String recordId;
    private String identityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitorBean = (VisitorBean) getIntent().getExtras().getSerializable("bean");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_visitor_manager_guard);
        binding.setVisitor(visitorBean);
        ButterKnife.bind(this);


        title.setText("访客管理");
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backResult();
                finish();
            }
        });

        recordId = visitorBean.recordId;
        identityCode = visitorBean.identityCode;
//        recordId = getIntent().getStringExtra("recordId");
        HashMap<String, String> map = new HashMap<>();
        map.put("recordId", recordId);
        NetworkRequest.request(map, CommonUrl.VISITOR_INFO, "visitor_info_guard");
        if (visitorBean.visitStatus == 3) {//拜访完成
            statusComplete.setVisibility(View.VISIBLE);
            statusScan.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(visitorBean.identityCode)) {//已经扫码入校
                scanIn.setClickable(false);
            }
            if (visitorBean.visitStatus == 2) {//拜访中
                verifyCodeTv.setBackgroundResource(R.drawable.yuanjiao_gray_cc);
                verifyCodeTv.setClickable(false);
                verifyCodeEd.setText("######");
                verifyCodeEd.setEnabled(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        backResult();
        super.onBackPressed();
    }

    private void backResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", visitorBean);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.scan_in, R.id.scan_out, R.id.verify_code})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.scan_in:
                if (visitorBean.visitStatus == 1) {//待拜访
                    ToastUtils.showShort("请先输入验证码");
                    return;
                }
                Intent intentIn = new Intent(this, MyCaptureActivity.class);
                startActivityForResult(intentIn, SCAN_IN);
                break;
            case R.id.scan_out:
                if (TextUtils.isEmpty(identityCode)) {
                    ToastUtils.showShort("请先发卡入校");
                    return;
                }
                Intent intentOut = new Intent(this, MyCaptureActivity.class);
                startActivityForResult(intentOut, SCAN_OUT);
                break;
            case R.id.verify_code:
                if (TextUtils.isEmpty(verifyCodeEd.getText().toString())) {
                    ToastUtils.showShort("请输入验证码");
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("recordId", recordId);
                map.put("verificationCode", verifyCodeEd.getText().toString());
                NetworkRequest.request(map, CommonUrl.VISITOR_VERIFY_CODE, "verify_code");
                dialog.show();
//                if(verifyCodeEd.getText() !=null &&verifyCodeEd.getText().toString().equals(visitorBean.verificationCode)) {
//                    ToastUtils.showShort("验证成功");
//                    verifyCodeTv.setBackgroundResource(R.drawable.yuanjiao_gray_cc);
//                    verifyCodeTv.setClickable(false);
//                    verifyCodeEd.setText("######");
//                    verifyCodeEd.setEnabled(false);
//                }else {
//                    ToastUtils.showShort("验证失败");
//                }
                break;
//            case R.id.visit_really_time_text:
//                TimeUtil.selectDateYMDHM(this, reallyInTimeTv);
//                break;
//            case R.id.visit_out_time_text:
//                TimeUtil.selectDateYMDHM(this, outTimeTv);
//                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "visitor_info_guard":
                JSONObject jsonObject = (JSONObject) event.getData();
                String teacherName = visitorBean.teacherName;
                visitorBean = GsonUtils.toObjetJson(jsonObject, VisitorBean.class);
                if (visitorBean != null) {
                    visitorBean.teacherName = teacherName;
                }
                binding.setVisitor(visitorBean);
                break;
            case "verify_code":
                ToastUtils.showShort("验证成功");
                verifyCodeTv.setBackgroundResource(R.drawable.yuanjiao_gray_cc);
                verifyCodeTv.setClickable(false);
                verifyCodeEd.setText("######");
                verifyCodeEd.setEnabled(false);
                visitorBean.visitStatus = 2;
                break;
            case "scan_in":
                scanIn.setClickable(false);
                visitorBean.identityCode = identityCode;
                visitorBean.scanCodeEnterTime = TimeUtil.getDateTimeWithMinute();
                ToastUtils.showShort("扫码成功，确认入校");
                break;
            case "scan_out":
                scanOut.setClickable(false);
                visitorBean.visitStatus = 3;
                visitorBean.scanCodeLeaveTime = TimeUtil.getDateTimeWithMinute();
                ToastUtils.showShort("扫码成功，确认离校");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_IN:
                scanResult(data, scanInTextView, IN);
                break;
            case SCAN_OUT:
                scanResult(data, scanOutTextView, OUT);
                break;
        }
    }

    private void scanResult(Intent data, TextView tv, int inOrOut) {
        if (data == null)
            return;
        int type = data.getExtras().getInt(CodeUtils.RESULT_TYPE);
        if (type == CodeUtils.RESULT_SUCCESS) {
            String code = data.getExtras().getString(CodeUtils.RESULT_STRING);
            identityCode = code;
            tv.setText(TimeUtil.getDateTimeWithMinute());
            HashMap<String, String> map = new HashMap<>();
            map.put("recordId", recordId);
            map.put("identityCode", code);
            if (inOrOut == IN) {
                NetworkRequest.request(map, CommonUrl.VISITOR_SCAN_IN, "scan_in");
                dialog.show();
//                reallyInTimeTv.setText(TimeUtil.getDateTimeWithMinute());
            } else if (inOrOut == OUT) {
                NetworkRequest.request(map, CommonUrl.VISITOR_SCAN_OUT, "scan_out");
                dialog.show();
//                outTimeTv.setText(TimeUtil.getDateTimeWithMinute());
            }
        } else {
            ToastUtils.showShort("扫描失败");
        }
    }
}
