package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonMoralEducationContentRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonMoralEducationListRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
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
 *个人德育编辑修改
 */
public class PersonMoralEducationContentEditorActvity extends BaseActivity {

    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.activity_person_moral_education_editor_time)
    TextView activityPersonMoralEducationEditorTime;
    @BindView(R.id.activity_person_moral_education_editor_project)
    TextView activityPersonMoralEducationEditorProject;
    @BindView(R.id.activity_person_moral_education_editor_ed)
    TextView activityPersonMoralEducationEditorEd;
    @BindView(R.id.activity_person_moral_education_editor_clazz)
    TextView activityPersonMoralEducationEditorClazz;
    @BindView(R.id.activity_person_moral_education_editor_head)
    ImageView activityPersonMoralEducationEditorHead;
    @BindView(R.id.activity_person_moral_education_editor_value_interval)
    TextView activityPersonMoralEducationEditorValueInterval;
    @BindView(R.id.activity_person_moral_education_editor_score)
    EditText activityPersonMoralEducationEditorScore;
    @BindView(R.id.activity_person_moral_education_editor_submit)
    TextView activityPersonMoralEducationEditorSubmit;
    @BindView(R.id.activity_person_moral_education_editor_name)
    TextView activityPersonMoralEducationEditorName;
    @BindView(R.id.activity_person_moral_education_editor_time_time)
    TextView activityPersonMoralEducationEditorTimeTime;
    private int valueBetween;
    private String result;//分数
    private String inspectorId;//督导员Id
    private String moralManId;
    private String scoreMode;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_moral_education_editor);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        titleTv.setText("德育专项检查");
        rightText.setVisibility(View.VISIBLE);
    }

    private void initData() {
        dialog.show();
        String id = getIntent().getStringExtra("person_moral_education_datail_id");
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
                                    scoreMode = o.optString("scoreMode");
                                    moralManId = o.optString("moralManId");
                                    inspectorId = o.optString("inspector");
                                    rightText.setText(o.optString("inspectorName"));
                                    activityPersonMoralEducationEditorTime.setText("检查时间：" + o.optString("checkTime").substring(0, 11));
                                    activityPersonMoralEducationEditorTimeTime.setText("日期：" + o.optString("checkTime").substring(0, 11));
                                    activityPersonMoralEducationEditorProject.setText("检查项目：" + o.optString("moralName"));
                                    activityPersonMoralEducationEditorEd.setText("学号:" + o.optString("certificateId"));
                                    activityPersonMoralEducationEditorClazz.setText(o.optString("className"));
//                                    activityPersonMoralEducationEditorScore.setText(o.optString("score"));
                                    activityPersonMoralEducationEditorName.setText("检查人：" + o.optString("inspectorName"));
                                    valueBetween = o.optInt("meterRange");
                                    if (Integer.parseInt(scoreMode) == 0) {
                                        if (valueBetween == 0) {
                                            activityPersonMoralEducationEditorValueInterval.setText("分值区间:" + valueBetween + "-0");
                                        } else {
                                            activityPersonMoralEducationEditorValueInterval.setText("分值区间:-" + valueBetween + "-0");
                                        }
                                    } else if (Integer.parseInt(scoreMode) == 1) {
                                        if (valueBetween == 0) {
                                            activityPersonMoralEducationEditorValueInterval.setText("分值区间:" + valueBetween + "-0");
                                        } else {
                                            activityPersonMoralEducationEditorValueInterval.setText("分值区间:0-" + valueBetween);
                                        }
                                    }

                                    GlideUtil.loadImageHead(o.optString("photoUrl"), activityPersonMoralEducationEditorHead);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case Config.EDUPERSON_MODIFY:
                dialog.dismiss();
                ToastUtils.toast("提交成功");
                EventBus.getDefault().post(new EventCenter<>("contentEditor", null));
                finish();
                break;
        }
    }

    @OnClick({R.id.title_back_img, R.id.activity_person_moral_education_editor_submit})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.activity_person_moral_education_editor_submit:
                LogUtil.e("score模式为" + scoreMode);
                LogUtil.e("区间分值为" + valueBetween);
                if (TextUtils.isEmpty(activityPersonMoralEducationEditorScore.getText().toString())) {
                    ToastUtils.showShort("输入不能为空");
                } else {
                    result = activityPersonMoralEducationEditorScore.getText().toString().trim();
                    if (Integer.parseInt(scoreMode) == 0) {//负数
                        if (Float.parseFloat(result) > 0 || Float.parseFloat(result) < -valueBetween) {
                            ToastUtils.showShort("请输入学分在-" + valueBetween + "到0之间");
                        } else {
                            dialog.show();
                            LogUtil.e("moralMainid" + moralManId);
                            LogUtil.e("inspectorId" + inspectorId);
                            PersonMoralEducationContentRequest personMoralEducationContentRequest = new PersonMoralEducationContentRequest(moralManId, inspectorId, result);
                            LogUtil.e("提交的参数列表", GsonUtils.toJson(personMoralEducationContentRequest));
                            NetworkRequest.request(personMoralEducationContentRequest, CommonUrl.EDUPERSON_MODIFY, Config.EDUPERSON_MODIFY);
                        }
                    } else if (Integer.parseInt(scoreMode) == 1) {//正数
                        if (Float.parseFloat(result) < 0 || Float.parseFloat(result) > valueBetween) {
                            ToastUtils.showShort("请输入学分在0到" + valueBetween + "之间");
                        } else {
                            dialog.show();
                            LogUtil.e("moralMainid" + moralManId);
                            LogUtil.e("inspectorId" + inspectorId);
                            PersonMoralEducationContentRequest personMoralEducationContentRequest = new PersonMoralEducationContentRequest(moralManId, inspectorId, result);
                            LogUtil.e("提交的参数列表", GsonUtils.toJson(personMoralEducationContentRequest));
                            NetworkRequest.request(personMoralEducationContentRequest, CommonUrl.EDUPERSON_MODIFY, Config.EDUPERSON_MODIFY);
                        }
                    }

                }
                break;
        }
    }
}
