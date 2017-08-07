package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SchoolParams;
import com.zjhz.teacher.NetworkRequests.response.SchoolBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的学校
 * Created by Administrator on 2016/7/8.
 */
public class MySchoolActivity extends BaseActivity {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.map)
    ImageView map;
    private final static String TAG = MySchoolActivity.class.getSimpleName();

    @BindView(R.id.school_name)
    TextView schoolName;
    @BindView(R.id.school_address)
    TextView schoolAddress;
    @BindView(R.id.school_domain)
    TextView schoolDomain;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myschool);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        titleTv.setText("我的学校");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.setMessage("正在获取信息...");
        dialog.show();
        getData();
    }
    private void getData() {
        NetworkRequest.request(new SchoolParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey)), "SysSchoolService.dtl", "school");
    }
    @Subscribe
    public void onEventMainThread(EventCenter ev) {
        dialog.dismiss();
        if (ev.getEventCode().equals("school")) {
            JSONObject jsonObject = (JSONObject) ev.getData();
            SchoolBean b = null;
            try {
                b = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(), SchoolBean.class);
                if (b != null){
                    if (!SharePreCache.isEmpty(b.getImageUrl())){
                        GlideUtil.loadImage(b.getImageUrl(),map);
                    }else {
                        map.setVisibility(View.GONE);
                    }
                    schoolName.setText("学校名称："+b.getSchoolName());
                    schoolAddress.setText("学校地址："+b.getAddress());
                    schoolDomain.setText("学校网址："+b.getDomain());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
