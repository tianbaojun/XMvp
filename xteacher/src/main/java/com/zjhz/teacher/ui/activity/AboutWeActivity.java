package com.zjhz.teacher.ui.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.CompanyBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpdateInfo;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于
 *  Created by  on 2016/6/2.
 */
public class AboutWeActivity  extends BaseActivity{
    private final static String TAG = AboutWeActivity.class.getSimpleName();

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.title_back_img)
    TextView back_icon;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.tv_about_version)
    TextView tv_about_version;

    @BindView(R.id.version_list)
    TextView versionListTv;
    @BindView(R.id.newest_version_ll)
    LinearLayout newVersionLL;
    @BindView(R.id.newest_version_title)
    TextView newVersionTitle;
    @BindView(R.id.newest_version)
    TextView newVersionName;
    @BindView(R.id.newest_version_flag)
    TextView newVersionFlag;

    private UpdateInfo updateInfo;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);

        title_tv.setText("关于");
        PackageInfo info =  BaseUtil.getPackageInfo(this);
        tv_about_version.setText("版本"+info.versionName);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        versionListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(VersionListActivity.class);
            }
        });

        newVersionLL.setOnClickListener(null);

        newVersion();

        getData();
    }

    private void newVersion(){
        updateInfo = (UpdateInfo)getIntent().getExtras().getSerializable("update_info");
        if(updateInfo != null){
            newVersionLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", updateInfo);
                    startActivity(NewestVersionActivity.class, bundle);
                }
            });
            newVersionTitle.setText("版本更新");
            newVersionName.setText(updateInfo.getVersionName());
            newVersionFlag.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        NetworkRequest.request(null,"SysParameterService.listSysParameter","dtl");
//        NetworkRequest.request(new CheckRequest("1", "TEACHER_APP"), CommonUrl.NEWEST_VERSION, "newest_version");
    }
    @Subscribe
    public void onEventMainThread(EventCenter ev) {
        JSONObject jsonObject = (JSONObject) ev.getData();
        switch (ev.getEventCode()) {
            case "dtl":

                CompanyBean b = null;
                try {
                    b = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(), CompanyBean.class);
                    if (b != null) {
                        company_name.setText(b.getCompany_zh() + "\ntel:" + b.getTel());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
//            case "newest_version":
//                try {
//                    final UpdateInfo updateInfo = new UpdateInfo();
//                    JSONObject data = jsonObject.optJSONObject("data");
//                    LogUtil.e("版本返回的data = ", data + "");
//                    if (data != null) {
//                        updateInfo.setApkUrl(data.optString("urlDownload"));
//                        updateInfo.setVersionName(data.optString("appVersion"));
//                        updateInfo.setChangeLog(data.optString("description"));
//                        updateInfo.setAppName("千校云");
//
//                        if (lowThanNewestVersion(BaseUtil.getPackageInfo(this).versionName, updateInfo.getVersionName())) {
//                            newVersionLL.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("bean", updateInfo);
//                                    startActivity(NewestVersionActivity.class, bundle);
//                                }
//                            });
//                            newVersionTitle.setText("版本更新");
//                            newVersionName.setText(updateInfo.getVersionName());
//                            newVersionFlag.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
        }
    }

//    //低于最新版本
//    private boolean lowThanNewestVersion(String currentVersion, String newestVersion) {
//        int length = Math.min(currentVersion.split("\\.").length, newestVersion.split("\\.").length);
//        if (TextUtils.isEmpty(currentVersion) || TextUtils.isEmpty(newestVersion) || length == 0)
//            return false;
//        for (int i = 0; i < length; i++) {
//            if (Integer.parseInt(currentVersion.split("\\.")[i]) < Integer.parseInt(newestVersion.split("\\.")[i])) {
//                return true;
//            }
//        }
//        return false;
//    }
}
