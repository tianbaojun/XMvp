package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.VisitorBean;
import com.zjhz.teacher.databinding.ActivityVisitorInfoBinding;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.TimeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisitorInfoActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    public TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView back_icon;
    @BindView(R.id.visit_finish_teacher)
    TextView visitFinishTv;
    private VisitorBean visitorBean;
    private String recordId;
    private ActivityVisitorInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitorBean = (VisitorBean) getIntent().getExtras().getSerializable("bean");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_visitor_info);
        binding.setVisitor(visitorBean);
        ButterKnife.bind(this);
        titleTv.setText("访客信息");

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        //TODO 通过接口得到访客信息明细
        recordId = visitorBean.recordId;
//        recordId = getIntent().getStringExtra("recordId");
        HashMap<String, String> map = new HashMap<>();
        map.put("recordId", recordId);
        NetworkRequest.request(map, CommonUrl.VISITOR_INFO, "visitor_info");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", visitorBean);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick(R.id.visit_finish_teacher)
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.visit_finish_teacher:
                //TODO 结束拜访接口
                HashMap<String, String> map = new HashMap<>();
                map.put("recordId", recordId);
                NetworkRequest.request(map, CommonUrl.VISITOR_END, "visitor_end");
                dialog.show();
                visitorBean.visitStatus = 3;
                visitorBean.leaveTime = TimeUtil.getDateTimeWithMinute();
                binding.notifyChange();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "visitor_info":
                JSONObject jsonObject = (JSONObject) event.getData();
                visitorBean = GsonUtils.toObjetJson(jsonObject, VisitorBean.class);
                binding.setVisitor(visitorBean);
                break;
            case "visitor_end":
                visitorBean.leaveTime = TimeUtil.getDateTimeWithMinute();
                visitorBean.visitStatus = 3;
                binding.setVisitor(visitorBean);
                break;
        }
    }
}
