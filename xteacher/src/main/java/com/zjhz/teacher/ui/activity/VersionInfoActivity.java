package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.bean.UpdateInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VersionInfoActivity extends BaseActivity {

    @BindView(R.id.title_back_img)
    TextView back_icon;
    @BindView(R.id.title_tv)
    TextView titileTv;
    @BindView(R.id.version_info)
    TextView versionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_info);
        ButterKnife.bind(this);
        titileTv.setText("系统通知");

        UpdateInfo updateInfo = (UpdateInfo) getIntent().getExtras().getSerializable("bean");

        versionInfo.setText(updateInfo != null ? updateInfo.getChangeLog() : "");

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }
}
