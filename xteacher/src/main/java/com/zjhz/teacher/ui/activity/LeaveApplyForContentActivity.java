package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.AgreeRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForContentModifyRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForContentRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForContentTypeRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForStateScheduleRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveTipsayAddRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.LeaveStateSchedule;
import com.zjhz.teacher.bean.LeaveStateScheduleBean;
import com.zjhz.teacher.bean.LeaveType;
import com.zjhz.teacher.bean.TipsayBean;
import com.zjhz.teacher.ui.delegate.LeaveApplyForContentDelegate;
import com.zjhz.teacher.ui.dialog.SelectTeacherDialog;
import com.zjhz.teacher.ui.view.ScrollAnimatedExpandableListView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.WheelUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请内容
 */
public class LeaveApplyForContentActivity extends BaseActivity implements WheelUtil.OnWheelClickSubmit {

    @BindView(R.id.title_text_left)
    public TextView left;
    @BindView(R.id.title_text_center)
    public TextView center;
    @BindView(R.id.title_right_text)
    public TextView right;
    @BindView(R.id.activity_leave_apply_for_start_time)
    public TextView startTime;
    @BindView(R.id.activity_leave_apply_for_end_time)
    public TextView endTime;
    @BindView(R.id.activity_leave_apply_for_type)
    public TextView type;
    @BindView(R.id.activity_leave_apply_for_image)
    RelativeLayout imageView;
    @BindView(R.id.activity_leave_apply_for_reason)
    public EditText editText;
    @BindView(R.id.activity_leave_apply_for_listView)
    public ScrollAnimatedExpandableListView listView;
    @BindView(R.id.kcap_layout)
    LinearLayout kcapLayout;//课程安排

    public LeaveApplyForContentDelegate delegate;
    public List<String> leaves = new ArrayList<>();
    public List<LeaveType> leaveTypes = new ArrayList<>();
    public Map<String, String> maps = new HashMap<>();
    public Map<String, String> tipsMaps = new HashMap<>();
    WheelUtil mWheelUtil;
    boolean isTime = false;
    private final static String TAG = LeaveApplyForContentActivity.class.getSimpleName();
    String timeStart;
    String timeEnd;
    String startFlag;
    String endFlag;
    String typeIntent;
    private String requestType, oid;

    private int status;
    public List<TipsayBean> tipsayBeans = new ArrayList<>();

    private List<LeaveTipsayAddRequest> tipsayAddRequestList = new ArrayList<>();//排课列表

    // 驳回
    String startTimeReject, endTimeReject, appTypeReject, reasonReject;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply_for);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        delegate = new LeaveApplyForContentDelegate(this);
        delegate.initialize();
        mWheelUtil = new WheelUtil();
        mWheelUtil.setOnClickSubmit(this);
        mWheelUtil.setType("1");
        mWheelUtil.initDatas();
        NetworkRequest.request(new LeaveApplyForContentTypeRequest("OA_HOLIDAY_TYPE"), CommonUrl.LEAVETYPEFLY, Config.LEAVETYPEFLY);
        Intent intent = getIntent();
        typeIntent = intent.getStringExtra("type");
        oid = intent.getStringExtra("leaver_list_adapter_oid");
        if ("1".equals(typeIntent)) {
            startTimeReject = intent.getStringExtra("leaver_list_adapter_startTime");// 起始时间
            endTimeReject = intent.getStringExtra("leaver_list_adapter_endTime");   // 截止时间
            appTypeReject = intent.getStringExtra("leaver_list_adapter_appType");  // 请假类型
            reasonReject = intent.getStringExtra("leaver_list_adapter_reason");   // 请假理由
        }
        startTime.setText(startTimeReject);
        endTime.setText(endTimeReject);
        type.setText(appTypeReject);

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "1");
        NetworkRequest.request(map, CommonUrl.LEAVE_ADD_SCHEDULE_STATUS, "leave_add_schedule_status");

        setTextChange(startTime);
        setTextChange(endTime);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //起始，结束时间影响课表列表
    private void setTextChange(TextView textView){
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(status == 1)
                    setPkListView();
            }
        });
    }

    private void setPkListView(){
        if(!TextUtils.isEmpty(startTime.getText().toString()) && !TextUtils.isEmpty(endTime.getText().toString())){
            String start = startTime.getText().toString().substring(0,11)+" 00:00:00";
            String end = endTime.getText().toString().substring(0,11)+" 00:00:00";
//            if("上午".equals(start))
//                start = startTime.getText().toString().substring(0,11)+"00:00:00";
//            else
//                start = startTime.getText().toString().substring(0,11)+"12:00:00";
//            if("上午".equals(end))
//                end = endTime.getText().toString().substring(0,11)+"12:00:00";
//            else
//                end = endTime.getText().toString().substring(0,11)+"24:00:00";
            NetworkRequest.request(new LeaveApplyForStateScheduleRequest(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey),
                    start, end, 1, 100), CommonUrl.LEAVE_ID_SCHEDULE, Config.LEAVE_ID_SCHEDULE);
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.ADDLEAVE:
                dialog.dismiss();
                ToastUtils.toast("添加成功");
                setResult(RESULT_OK);
                finish();
                break;
            case Config.SUPPLYTEACHER:
                dialog.dismiss();
                JSONObject schedule1 = (JSONObject) event.getData();
                if (schedule1 != null) {
                    tipsayBeans.clear();
                    tipsMaps.clear();
                    List<TipsayBean> beans = GsonUtils.toArray(TipsayBean.class, schedule1);
                    if (beans != null) {
                        tipsayBeans.addAll(beans);
                        for (TipsayBean bean : beans) {
                            tipsMaps.put(bean.getName(), bean.getTeacherId());
                        }
                    }

                }
                SelectTeacherDialog selectTeacherDialog = new SelectTeacherDialog(this, -1, tipsayBeans);  // 代课老师对话框
                selectTeacherDialog.setGroupChild(-1);
                break;
            case Config.LEAVETYPEFLY:    // TODO 成功获取到请假的类型数据
                dialog.dismiss();
                leaves.clear();
                JSONObject data = (JSONObject) event.getData();
                if (data != null) {
                    JSONArray data1 = data.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                LeaveType mLeaveType = new LeaveType();
                                mLeaveType.paramKey = o.optString("paramKey");
                                mLeaveType.paramValue = o.optString("paramValue");
                                LogUtil.e("请假类型", o.optString("paramKey") + "-------" + o.optString("paramValue"));
                                maps.put(o.optString("paramValue"), o.optString("paramKey"));
                                leaveTypes.add(mLeaveType);
                                leaves.add(o.optString("paramValue"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case Config.MODIFYREJECTAPPLY:
//                dialog.dismiss();
                ToastUtils.toast("修改成功");
                NetworkRequest.request(new AgreeRequest(oid, editText.getText().toString().trim()), CommonUrl.REJECTAPPLY, Config.REJECTAPPLY);
                finish();
                break;
            case Config.REJECTAPPLY:
                dialog.dismiss();
                ToastUtils.toast("申请成功");
                break;
            case Config.LEAVE_ID_SCHEDULE:   // TODO 排课表
                dialog.dismiss();
                delegate.lists.clear();
                JSONObject fly_data = (JSONObject) event.getData();
                LogUtil.e("请假排课表返回数据 = ", fly_data.toString());
                if (fly_data != null) {
                    JSONArray data1 = fly_data.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                if (o != null) {
                                    LeaveStateSchedule mLeaveStateSchedule = new LeaveStateSchedule();
                                    mLeaveStateSchedule.date = o.optString("date");
                                    JSONArray schedule = o.optJSONArray("schedule");
                                    if (schedule != null && schedule.length() > 0) {
                                        for (int j = 0; j < schedule.length(); j++) {
                                            JSONObject o1 = (JSONObject) schedule.get(j);
                                            if (o1 != null) {
                                                LeaveStateScheduleBean mLeaveStateScheduleBean = new LeaveStateScheduleBean();
                                                mLeaveStateScheduleBean.clazz = o1.optString("clazz");
                                                mLeaveStateScheduleBean.week = o1.optString("week");
                                                mLeaveStateScheduleBean.subject = o1.optString("subjectName");
                                                mLeaveStateScheduleBean.className = o1.optString("className");
                                                mLeaveStateScheduleBean.schoolId = o1.optString("schoolId");
                                                mLeaveStateScheduleBean.teacherId = o1.optString("teacherId");
                                                mLeaveStateScheduleBean.classId = o1.optString("classId");
                                                mLeaveStateScheduleBean.subjectId = o1.optString("subjectId");
                                                mLeaveStateSchedule.lists.add(mLeaveStateScheduleBean);
                                            }
                                        }
                                    }
                                    delegate.lists.add(mLeaveStateSchedule);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    delegate.initList();
                    listView.expandGroup(0);
                }
                break;
            case Config.TEACHERFLY:
                // 代课老师名称
                Map<String,String> eventData = (Map<String,String>) event.getData();
                String nameTipsay = eventData.get("value");
                int groupChild = Integer.parseInt(eventData.get("position"));

                delegate.lists.get(delegate.mGroupPosition).lists.get(delegate.mChildPosition).tipsay = nameTipsay;
                delegate.initList(); // 刷新适配器

                LeaveStateScheduleBean bean = delegate.lists.get(delegate.mGroupPosition).lists.get(delegate.mChildPosition);
                String schoolId =bean .schoolId;
                String teacherId =bean.teacherId;
                String week = bean.week;
                String clazz = bean.clazz;
                String subjectId =bean.subjectId;
                String classId =bean.classId;
                String linkId =bean.linkId;
                String linkTime = delegate.lists.get(delegate.mGroupPosition).date;
                String tipsayTeacherId = tipsMaps.get(nameTipsay);

                LeaveTipsayAddRequest mLeaveTipsayAddRequest = new LeaveTipsayAddRequest(linkTime,classId,schoolId,oid,teacherId,week,clazz,subjectId,tipsayTeacherId);
                mLeaveTipsayAddRequest.groupChild = groupChild;
                boolean isReplace = false;
                for(int i = 0; i < tipsayAddRequestList.size(); i++){
                    if(tipsayAddRequestList.get(i).groupChild == groupChild) {
                        tipsayAddRequestList.set(i, mLeaveTipsayAddRequest);
                        isReplace = true;
                    }
                }
                if(!isReplace)
                    tipsayAddRequestList.add(mLeaveTipsayAddRequest);
//                NetworkRequest.request(mLeaveTipsayAddRequest, CommonUrl.LEAVE_TIPSAY_ADD, Config.LEAVE_TIPSAY_ADD);
                break;
            case Config.LEAVE_TIPSAY_ADD:

                break;
            case "leave_add_schedule_status":
                try {
                    JSONObject jb = ((JSONObject) event.getData()).getJSONObject("data").getJSONObject("entity");
                    status = jb.optInt("status");

                    if (status == 1) {
                        kcapLayout.setVisibility(View.VISIBLE);
                    } else {
                        kcapLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "LEAVE_TIPSAY_ADD_LIST":
                try {
                    JSONObject data1 = ((JSONObject)event.getData()).getJSONObject("data");
                    String linkIds = data1.optString("linkIds");
                    LeaveApplyForContentRequest leaveApplyForContentRequest = new LeaveApplyForContentRequest(
                            "1", "1", TimeUtil.refFormatNowDate(), sTime, startFlag, eTime, endFlag, requestType,
                            editText.getText().toString().trim(), "1", "1", "1", "1", linkIds);
                    NetworkRequest.request(leaveApplyForContentRequest, CommonUrl.ADDLEAVE, Config.ADDLEAVE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @OnClick({R.id.title_text_left, R.id.title_right_text, R.id.activity_leave_apply_for_start_time,
            R.id.activity_leave_apply_for_end_time, R.id.activity_leave_apply_for_image})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_text_left:
                finish();
                break;
            //// TODO: 2017/3/1 修复重复提交bug
            case R.id.title_right_text:  //  TODO 网络请求
                if ("2".equals(typeIntent)) {
                    requestType = maps.get(type.getText().toString());
                    if(TextUtils.isEmpty(sTime) || TextUtils.isEmpty(eTime)){
                        ToastUtils.toast("请假时间不能为空");
                        return;
                    }
                    //开始时间小于结束时间
                    if ((startInt > endInt) || (startInt == endInt && am > pm)) {
                        ToastUtils.toast("起始时间不能大于截止时间");
                        return;
                    }
                    if(TextUtils.isEmpty(requestType)){
                        ToastUtils.toast("请假类型不能为空");
                        return;
                    }
                    if(TextUtils.isEmpty(editText.getText().toString())){
                        ToastUtils.showShort("请假事由不能为空");
                        return;
                    }

                    //如果开始提交则设置不能点击
                    view.setClickable(false);
                    try {
                        if (!dialog.isShowing()) {
                            //显示加载框
                            dialog.show();
                        }

                        LeaveApplyForContentRequest leaveApplyForContentRequest = new LeaveApplyForContentRequest("1", "1", TimeUtil.refFormatNowDate(), sTime, startFlag, eTime, endFlag, requestType, editText.getText().toString().trim(), "1", "1", "1", "1");
                        LogUtil.e("请假申请内容", GsonUtils.toJson(leaveApplyForContentRequest));

                        if (status == 1 && tipsayAddRequestList.size() > 0) {
                            Map<String, String> map = new HashMap<>();
                            map.put("useId", SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey));
                            map.put("schoolId", SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey));
                            map.put("oaLinkedParam", GsonUtils.toJson(tipsayAddRequestList));
                            NetworkRequest.request(map, CommonUrl.LEAVE_TIPSAY_ADD_LIST, "LEAVE_TIPSAY_ADD_LIST");
                        } else
                            NetworkRequest.request(leaveApplyForContentRequest, CommonUrl.ADDLEAVE, Config.ADDLEAVE);

                    } finally {
                        view.setClickable(true);
                    }
                } else {  // 驳回接口
                    dialog.show();
                    LeaveApplyForContentModifyRequest leaveApplyForContentModifyRequest = new LeaveApplyForContentModifyRequest(oid, "1", "1", TimeUtil.refFormatNowDate(), sTime, startFlag, eTime, endFlag, requestType, editText.getText().toString().trim(), "1", "1", "1", "1");
                    LogUtil.e("修改请假申请内容", GsonUtils.toJson(leaveApplyForContentModifyRequest));
                    NetworkRequest.request(leaveApplyForContentModifyRequest, CommonUrl.MODIFYREJECTAPPLY, Config.MODIFYREJECTAPPLY);
                }
                break;
            case R.id.activity_leave_apply_for_start_time:
                isTime = true;
                mWheelUtil.selectData(LeaveApplyForContentActivity.this);
                break;
            case R.id.activity_leave_apply_for_end_time:
                isTime = false;
                if (!TextUtils.isEmpty(startTime.getText().toString())) {
                    mWheelUtil.selectData(LeaveApplyForContentActivity.this);
                } else {
                    ToastUtils.toast("开始时间不能为空");
                }
                break;
            case R.id.activity_leave_apply_for_image:
                BaseUtil.selectSubject(leaves, this, type);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegate = null;
    }

    public int startInt;
    public int endInt;
    public String sTime, eTime;
    int am;  // 开始的上下午
    int pm;  // 截止的上下午

    @Override
    public void onClickSubmit() {
        LogUtil.e("请假内容", mWheelUtil.historyDate);
        if (isTime) {
            timeStart = mWheelUtil.historyDate;
            startInt = Integer.parseInt(mWheelUtil.historyDateInt);
            startFlag = (Integer.parseInt(mWheelUtil.flag) + 1) + "";
            sTime = mWheelUtil.historyDate;
            if (mWheelUtil.flag != null) {
                am = Integer.parseInt(mWheelUtil.flag);
                if (Integer.parseInt(mWheelUtil.flag) == 0) {
                    startTime.setText(mWheelUtil.historyDate + " 上午");
                } else {
                    startTime.setText(mWheelUtil.historyDate + " 下午");
                }
            }
        } else {
            timeEnd = mWheelUtil.historyDate;
            endInt = Integer.parseInt(mWheelUtil.historyDateInt);
            endFlag = (Integer.parseInt(mWheelUtil.flag) + 1) + "";
            eTime = mWheelUtil.historyDate;
            if (mWheelUtil.flag != null) {
                pm = Integer.parseInt(mWheelUtil.flag);
                if (Integer.parseInt(mWheelUtil.flag) == 0) {
                    endTime.setText(mWheelUtil.historyDate + " 上午");
                } else {
                    endTime.setText(mWheelUtil.historyDate + " 下午");
                }
            }
//           NetworkRequest.request(new SupplyTeacherRequest(sTime,eTime), CommonUrl.SUPPLYTEACHER, Config.SUPPLYTEACHER);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("LeaveApplyForContent Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
