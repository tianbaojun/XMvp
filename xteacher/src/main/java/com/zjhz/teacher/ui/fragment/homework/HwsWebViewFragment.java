package com.zjhz.teacher.ui.fragment.homework;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import butterknife.BindView;


public class HwsWebViewFragment extends BaseFragment {

    private static final String CLASS_ID = "class_id";
    private static final String HOMEWORK_ID = "homework_id";
    @BindView(R.id.homework_wb)
    WebView webView;

    private boolean isLoadView;

    public HwsWebViewFragment() {
        // Required empty public constructor
    }

    public static HwsWebViewFragment newInstance(String homeworkId,String classId) {
        HwsWebViewFragment fragment = new HwsWebViewFragment();
        Bundle args = new Bundle();
        args.putString(HOMEWORK_ID, homeworkId);
        args.putString(CLASS_ID, classId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_hws_webview, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        if(!isLoadView){
            loadWebView();
        }
        isLoadView = true;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void lazyLoad() {
        if(isLoadView){
            loadWebView();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView(){
        if(getArguments() == null)
            return;
        String token = SharedPreferencesUtils.getSharePrefString(ConstantKey.TokenKey);
        String url = CommonUrl.BASEURL.substring(0, CommonUrl.BASEURL.length() - 4) + "/template/mobile/apps/statistics/index.html?token=" + token+"&homeworkId="+getArguments().getString(HOMEWORK_ID);
        if(!TextUtils.isEmpty(getArguments().getString(CLASS_ID))) {
            url += "&classId=" + getArguments().getString(CLASS_ID);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        showDialog();
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress){
                if(newProgress == 100){
                    if(dialog != null && dialog.isShowing())
                        dialog.dismiss();
                }
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {

    }

}
