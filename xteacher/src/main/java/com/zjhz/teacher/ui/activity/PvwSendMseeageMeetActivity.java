package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-16
 * Time: 15:57
 * Description: 群发消息  预览
 */
public class PvwSendMseeageMeetActivity extends BaseActivity {
    private final static String TAG = PvwSendMseeageMeetActivity.class.getSimpleName();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.activity_pvw_send_text)
    TextView activityPvwSendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvw_send_mseeage);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        titleTv.setText("会议通知");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String name = getIntent().getStringExtra("name");
        activityPvwSendText.setText("接收对象："+name);
    }
    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }
}
