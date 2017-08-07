package com.zjhz.teacher.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgreementActivity extends BaseActivity {

    @BindView(R.id.title_back_img)
    TextView back;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.webView)
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTv.setText("用户隐私协议");
        webView.loadUrl(CommonUrl.BASEURL.substring(0, CommonUrl.BASEURL.length() - 4)+"/template/mobile/apps/reade/");
        WebSettings WebSettings = webView.getSettings();
        WebSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }
}