package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonMoralEducationListRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-02
 * Time: 10:57
 * Description: 个人德育    详情
 */
public class PersonMoralEducationDetailActivity extends BaseActivity {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;

    private final static String TAG = PersonMoralEducationDetailActivity.class.getSimpleName();
    @BindView(R.id.activity_person_moral_education_detail_time)
    TextView activityPersonMoralEducationDetailTime;
    @BindView(R.id.activity_person_moral_education_detail_project)
    TextView activityPersonMoralEducationDetailProject;
    @BindView(R.id.activity_person_moral_education_detail_ed)
    TextView activityPersonMoralEducationDetailEd;
    @BindView(R.id.activity_person_moral_education_detail_clazz)
    TextView activityPersonMoralEducationDetailClazz;
    @BindView(R.id.activity_person_moral_education_detail_head)
    ImageView image;
    @BindView(R.id.activity_person_moral_education_detail_score)
    TextView activityPersonMoralEducationDetailScore;
    @BindView(R.id.activity_person_moral_education_detail_name)
    TextView activityPersonMoralEducationDetailName;
    @BindView(R.id.activity_person_moral_education_detail_time_time)
    TextView activityPersonMoralEducationDetailTimeTime;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.activity_person_moral_education_detail_score_between)
    TextView activityPersonMoralEducationDetailScoreBetween;
    private int meterModeType;
    private String id;
    private HintPopwindow hintPopwindow;
    private String moralId;
    private String userId;
    private String createUser;
    private int scoreMode;
    private int meterRange;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_moral_education_detail);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        initView();
        initData();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private void initView() {
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTv.setText("德育专项检查");
        rightText.setVisibility(View.VISIBLE);
    }

    private void initData() {
        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
        dialog.show();
        id = getIntent().getStringExtra("person_moral_education_datail_id");
        PersonMoralEducationListRequest personMoralEducationListRequest = new PersonMoralEducationListRequest(id);
        LogUtil.e("个人德育列表请求参数", GsonUtils.toJson(personMoralEducationListRequest));
        NetworkRequest.request(personMoralEducationListRequest, CommonUrl.PERSON_EDUCATION, Config.PERSON_EDUCATION_DETAIL);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.toast("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.PERSON_EDUCATION_DETAIL:
                dialog.dismiss();
                JSONObject list = (JSONObject) event.getData();
                if (list != null) {
                    JSONArray data = list.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                if (o != null) {
                                    createUser = o.optString("createUser");
                                    activityPersonMoralEducationDetailTime.setText("检查时间：" + o.optString("checkTime").substring(5, 16));
                                    activityPersonMoralEducationDetailTimeTime.setText("日期：" + o.optString("checkTime").substring(0, 11));
                                    activityPersonMoralEducationDetailProject.setText("方案项目：" + o.optString("moralName"));
                                    activityPersonMoralEducationDetailEd.setText("学号:" + o.optString("certificateId"));
                                    activityPersonMoralEducationDetailClazz.setText("检查对象:" + o.optString("className"));
                                    activityPersonMoralEducationDetailScore.setText(o.optString("targetorName")+"：" + o.optString("score"));
                                    activityPersonMoralEducationDetailName.setText("检查人：" + o.optString("inspectorName"));
                                    moralId = o.optString("moralId");
                                    meterModeType = o.optInt("meterMode");
                                    scoreMode = o.optInt("scoreMode");
                                    meterRange = o.optInt("meterRange");
                                    LogUtil.e("meterModeType" + meterModeType);
                                    if (meterModeType == 0) {
                                        rightText.setText(R.string.no_cut_off);
                                    } else if (meterModeType == 1) {
                                        rightText.setText(R.string.mEdit);
                                        handleBetween();
                                    }
                                    GlideUtil.loadImageHead(o.optString("photoUrl"), image);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case Config.EDUPERSON_DELETE:
                dialog.dismiss();
                ToastUtils.showShort("删除成功");
                finish();
                EventBus.getDefault().post(new EventCenter<>("delete"));
                break;
            case "contentEditor":
                EventBus.getDefault().post(new EventCenter<>("listContentEditor", null));
                finish();
                break;
        }
    }

    private void handleBetween() {
        activityPersonMoralEducationDetailScoreBetween.setVisibility(View.VISIBLE);
        if (scoreMode == 0) {
            if (meterRange == 0) {
                activityPersonMoralEducationDetailScoreBetween.setText("分值区间:" + meterRange + "-0");
            } else {
                activityPersonMoralEducationDetailScoreBetween.setText("分值区间:-" + meterRange + "-0");
            }
        } else if (scoreMode == 1) {
            if (meterRange == 0) {
                activityPersonMoralEducationDetailScoreBetween.setText("分值区间:" + meterRange + "-0");
            } else {
                activityPersonMoralEducationDetailScoreBetween.setText("分值区间:0-" + meterRange);
            }
        }


    }


    @OnClick(R.id.right_text)
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        LogUtil.e("教师id " + userId + "创建者id" + createUser);
        if (meterModeType == 1) {
            if (userId.equals(createUser)) {
                startActivity(PersonMoralEducationContentEditorActvity.class, "person_moral_education_datail_id", id);
            } else {
                ToastUtils.showShort("没有编辑权限");
            }

        } else if (meterModeType == 0) {
            if (userId.equals(createUser)) {

                if (hintPopwindow == null) {
                    hintPopwindow = new HintPopwindow(this);
                    hintPopwindow.setTitleMessage("确认删除此专项检查吗?");
                }
                hintPopwindow.setOnclicks(new HintPopwindow.OnClicks() {
                    @Override
                    public void sureClick() {
                        hintPopwindow.dismiss();
                        dialog.setMessage("正在删除...");
                        dialog.show();
                        PersonMoralEducationListRequest personMoralEducationListRequest = new PersonMoralEducationListRequest(id);
                        NetworkRequest.request(personMoralEducationListRequest, CommonUrl.EDUPERSON_DELETE, Config.EDUPERSON_DELETE);
                    }

                    @Override
                    public void cancelClick() {
                        hintPopwindow.dismiss();
                    }
                });
                if (!hintPopwindow.isShowing()) {
                    hintPopwindow.showAtLocation(ll, Gravity.CENTER, 0, 0);
                }
            } else {
                ToastUtils.showShort("没有权限删除");
            }
        }
    }
}
