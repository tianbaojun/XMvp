package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.NewsDetailParams;
import com.zjhz.teacher.NetworkRequests.response.NewsBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.StringUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

/**
 * 通知公告详情
 * Created by xiangxue on 2016/6/16.
 */
public class AnnounceDetailActivity extends BaseActivity {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.news_title)
    TextView newsTitle;
    @BindView(R.id.news_time)
    TextView newsTime;
    @BindView(R.id.news_creat_user)
    TextView newsCreatUser;
    @BindView(R.id.web)
    WebView web;
    private String announceId ="";
    private final static String TAG = AnnounceDetailActivity.class.getSimpleName();

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        announceId = getIntent().getStringExtra("announceId");
        if (SharePreCache.isEmpty(announceId)) {
            ToastUtils.showShort("通知ID丢失");
            return;
        }
        dialog.setMessage("正在获取详情...");
        dialog.show();
        initTitle();
        getData();
    }

    private void initTitle() {
        titleTv.setText("通知公告");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        NetworkRequest.request(new NewsDetailParams(announceId), CommonUrl.announcementDetail, Config.announcedetail);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()){
            case  Config.ERROR:
                dialog.dismiss();
                ToastUtils.showShort("请求错误");
                break;
            case  Config.NOSUCCESS:
                dialog.dismiss();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1){
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey,"");
//                    toLoginActivity();
//                }
                break;
            case  Config.announcedetail:
                dialog.dismiss();
                JSONObject json = (JSONObject) event.getData();
                try {
                    NewsBean bean = GsonUtils.toObject(json.getJSONObject("data").toString(),NewsBean.class);
                    initData(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void initData( NewsBean bean) {
        if (bean != null){
            newsTitle.setText(bean.getTitle());
            String date = DateUtil.getStandardDate(bean.getPublishTime());
            newsTime.setText(date);
            newsCreatUser.setText(bean.getCreateUserVal());
            web.loadDataWithBaseURL(null, StringUtils.getNewContent( bean.getContent()),"text/html","UTF-8",null);
            Log.e("网络加载出来的数据",bean.getContent());
            Log.e("网络加载出来的数据",html.toString());

        }
    }




}
