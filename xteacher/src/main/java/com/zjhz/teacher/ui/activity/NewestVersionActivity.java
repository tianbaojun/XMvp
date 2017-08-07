package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.bean.UpdateInfo;
import com.zjhz.teacher.utils.OnUpdateListener;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.UpdateManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewestVersionActivity extends BaseActivity {

    @BindView(R.id.title_back_img)
    TextView back_icon;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.newest_version_one)
    TextView newestVersionOne;
    @BindView(R.id.newest_version_two)
    TextView newestVersionTwo;
    @BindView(R.id.newest_version_msg)
    TextView newestVersionMsg;
    @BindView(R.id.newest_version_update_btn)
    TextView updateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newest_version);
        ButterKnife.bind(this);

        titleTv.setText("更新版本");

        final UpdateInfo info = (UpdateInfo) getIntent().getExtras().getSerializable("bean");

        if (info != null) {
            newestVersionOne.setText(info.getVersionName());
            newestVersionTwo.setText(info.getVersionName());
            newestVersionMsg.setText(info.getChangeLog());
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateManager updateManager = new UpdateManager(NewestVersionActivity.this, info, new MyUpdateListener(), true);
                    updateManager.update();
                }
            });
        }

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

    private class MyUpdateListener implements OnUpdateListener {

        @Override
        public void onStartCheck() {

        }

        @Override
        public void onFinishCheck(UpdateInfo info, boolean isUpdateLowestVersion) {
            if (!TextUtils.isEmpty(info.getApkUrl())) {
                updateBtn.setOnClickListener(null);
                updateBtn.setBackgroundResource(R.drawable.btn_not_clickable_bg);
            }
        }

        @Override
        public void onStartDownload() {
            ToastUtils.showShort("正在下载更新");
        }

        @Override
        public void onDownloading(int progress) {

        }

        @Override
        public void onFinshDownload() {

        }
    }
}
