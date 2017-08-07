package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.AgreeRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForContentModifyRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForContentTypeRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForStateRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForStateScheduleRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveCheckPageRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveTipsayAddRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.LeaveApplyStateListAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.LeaveStateSchedule;
import com.zjhz.teacher.bean.LeaveStateScheduleBean;
import com.zjhz.teacher.bean.RepairsProposerState;
import com.zjhz.teacher.bean.TipsayBean;
import com.zjhz.teacher.ui.delegate.LeaveApplyForStateRejectDelegate;
import com.zjhz.teacher.ui.dialog.SelectTeacherDialog;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.ScrollAnimatedExpandableListView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.WheelUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

//import com.zjhz.app.adapter.RepairsProposerStateAdapter;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 请假申请详情(驳回)
 */
public class LeaveApplyForStateRejectActivity extends BaseActivity implements WheelUtil.OnWheelClickSubmit {

    private final static String TAG = LeaveApplyForStateRejectActivity.class.getSimpleName();
    @BindView(R.id.activity_leave_apply_for_state_image_reject)
    public CircleImageView image;
    @BindView(R.id.activity_leave_apply_for_state_time_reject)
    public TextView applyTime;
    @BindView(R.id.activity_leave_apply_for_start_time_reject)
    public TextView leaveStartTime; // 请假起始时间
    @BindView(R.id.activity_leave_apply_for_end_time_reject)
    public TextView leaveEndTime;   // 请假截止时间
    @BindView(R.id.activity_leave_apply_for_type_reject)
    public TextView leaveType;   // 请假类型
    @BindView(R.id.activity_leave_apply_for_reason_reject)  // 请假理由
    public EditText leaveReason;
    @BindView(R.id.activity_leave_apply_for_state_expandable_list_reject)
    public ScrollAnimatedExpandableListView expandableListView;
    public List<String> leaves = new ArrayList<>();
    public Map<String, String> maps = new HashMap<>();
    public int startInt;
    public int endInt;
    public String sTime, eTime;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.activity_leave_apply_for_state_name_reject)
    TextView name;
    @BindView(R.id.activity_leave_apply_for_state_list_reject)
    ScrollViewWithListView listView;
    List<RepairsProposerState> lists = new ArrayList<>();
    LeaveApplyStateListAdapter adapter;
    LeaveApplyForStateRejectDelegate delegate;
    @BindView(R.id.activity_leave_apply_for_state_agree)
    TextView agree;
    @BindView(R.id.activity_leave_apply_for_state_disagree)
    TextView disagree;
    @BindView(R.id.activity_leave_apply_for_state_linear)
    LinearLayout linearLayout;
    boolean checkFlag;
    WheelUtil mWheelUtil;
    boolean isTime = false;
    boolean isCheck = false;
    String requestType;
    String timeStart;
    String timeEnd;
    String startFlag;
    String endFlag;
    int am;  // 开始的上下午
    int pm;  // 截止的上下午
    private String oid, approveFlag, oldApplyTime;
    private String applyerTeacherId;
    private String startLeaveTime = "";
    private int status;//是否可以提交的时候排课
    public List<TipsayBean> tipsayBeans = new ArrayList<>();
    public Map<String, String> tipsMaps = new HashMap<>();
    private List<LeaveTipsayAddRequest> tipsayAddRequestList = new ArrayList<>();//排课列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply_for_state_reject);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText("提交");
        delegate = new LeaveApplyForStateRejectDelegate(this);
        delegate.initialize();
        mWheelUtil = new WheelUtil();
        mWheelUtil.setOnClickSubmit(this);
        mWheelUtil.setType("1");
        mWheelUtil.initDatas();

        HashMap<String, String> map = new HashMap<>();
        map.put("type", "1");
        NetworkRequest.request(map, CommonUrl.LEAVE_ADD_SCHEDULE_STATUS, "leave_add_schedule_status");
        setTextChange(leaveStartTime);
        setTextChange(leaveEndTime);
        initData();
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
                if(status != 1)
                    return;
                int length = delegate.lists.size();
                boolean isBefore = false, isAfter = false;
                if(length > 0) {
                    int before = DateUtil.compareDate(delegate.lists.get(0).date, leaveStartTime.getText().toString().substring(0, 11));
                    if (before == -1 || before == 0) {
                        isBefore = true;
                    }
                    int after = DateUtil.compareDate(delegate.lists.get(length -1).date, leaveEndTime.getText().toString().substring(0, 11));
                    if (after == 1 || after == 0) {
                        isAfter = true;
                    }
                }

                if(isBefore && isAfter)
                    updatePkList();
                else
                    setPkListView();
            }
        });
    }

    private void setPkListView(){
        if(!TextUtils.isEmpty(leaveStartTime.getText().toString()) && !TextUtils.isEmpty(leaveEndTime.getText().toString())){
            String start = leaveStartTime.getText().toString().substring(0,11)+" 00:00:00";
            String end = leaveEndTime.getText().toString().substring(0,11)+" 00:00:00";
            NetworkRequest.request(new LeaveApplyForStateScheduleRequest(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey),
                    start, end, 1, 100), CommonUrl.LEAVE_ID_SCHEDULE, Config.LEAVE_ID_SCHEDULE);
        }
    }

    private void initData() {
        titleTv.setText(R.string.application_for_leave);
        leaves.add("婚假");
        leaves.add("病假");
        leaves.add("事假");
        Intent intent = getIntent();
//        oid = intent.getStringExtra("leave_list_oid");
        oid = intent.getStringExtra("leaver_list_adapter_oid");
//        boolean isChecked = intent.getBooleanExtra("leave_list_isCheck",false);
        String ema = "";
        if (intent.getIntExtra("leaver_list_adapter_endTime_ema",0) == 1) {
            ema = " 上午";
        }else{
            ema = " 下午";
        }
        String sma = "";
        if (intent.getIntExtra("leaver_list_adapter_startTime_sma",0) == 1) {
            sma = " 上午";
        }else{
            sma = " 下午";
        }
        am = intent.getIntExtra("leaver_list_adapter_startTime_sma",0);
        pm = intent.getIntExtra("leaver_list_adapter_endTime_ema",0);
        sTime = intent.getStringExtra("leaver_list_adapter_startTime").substring(0,11).trim();
        String start = intent.getStringExtra("leaver_list_adapter_startTime").substring(0,11).trim().replace("-","");
        startInt = Integer.parseInt(start);
        eTime = intent.getStringExtra("leaver_list_adapter_endTime").substring(0,11).trim();
        String end = intent.getStringExtra("leaver_list_adapter_endTime").substring(0,11).trim().replace("-","");
        endInt = Integer.parseInt(end);
        requestType = intent.getStringExtra("leaver_list_adapter_appType");
        leaveStartTime.setText(intent.getStringExtra("leaver_list_adapter_startTime").substring(0,11) + sma);  // 起始时间
        leaveEndTime.setText(intent.getStringExtra("leaver_list_adapter_endTime").substring(0,11) + ema);  // 截止时间
        leaveType.setText(intent.getStringExtra("leaver_list_adapter_appType"));  // 请假类型
        leaveReason.setText(intent.getStringExtra("leaver_list_adapter_reason"));  // 请假理由
        isCheck = true;
        LogUtil.e("LeaveApplyForStateActivity",isCheck + "");
        dialog.show();
        NetworkRequest.request(new LeaveApplyForContentTypeRequest("OA_HOLIDAY_TYPE"), CommonUrl.LEAVETYPEFLY, Config.LEAVETYPEFLYFLY);
        if (isCheck) {
            LogUtil.e("请假详情","+++++");
            linearLayout.setVisibility(View.INVISIBLE);
            NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,1,10), CommonUrl.LEAVEDETAIL,Config.LEAVEDETAIL); // 请假详情
        }else{
            LogUtil.e("审批详情","-----");
            approveFlag = intent.getStringExtra("leave_list_approveFlag");
            if ("1".equals(approveFlag)) { // 可审批
                agree.setClickable(true);
                agree.setBackgroundResource(R.drawable.yuanjiao_red);
                disagree.setClickable(true);
                disagree.setBackgroundResource(R.drawable.yuanjiao_red);
            }else if ("2".equals(approveFlag)) {
                agree.setClickable(false);
                agree.setBackgroundResource(R.drawable.yuanjiao_gray);
                disagree.setClickable(false);
                disagree.setBackgroundResource(R.drawable.yuanjiao_gray);
            }
            linearLayout.setVisibility(View.VISIBLE);
            NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,1,10), CommonUrl.LEAVECHECKDETAIL,Config.LEAVECHECKDETAIL);
        }

        adapter = new LeaveApplyStateListAdapter(this, lists);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setAdapter(adapter);
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
            case Config.LEAVETYPEFLYFLY:
                leaves.clear();
                JSONObject dataFly = (JSONObject) event.getData();
                if (dataFly != null) {
                    JSONArray data1 = dataFly.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                maps.put(o.optString("paramValue"),o.optString("paramKey"));
                                leaves.add(o.optString("paramValue"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case Config.AGREE:
                dialog.dismiss();
                ToastUtils.toast("同意");
                agree.setClickable(false);
                agree.setBackgroundResource(R.drawable.yuanjiao_gray);
                disagree.setClickable(false);
                disagree.setBackgroundResource(R.drawable.yuanjiao_gray);
//                linearLayout.setVisibility(View.INVISIBLE);
                break;
            case Config.UNAGREE:
                dialog.dismiss();
                ToastUtils.toast("不同意");
                agree.setClickable(false);
                agree.setBackgroundResource(R.drawable.yuanjiao_gray);
                disagree.setClickable(false);
                disagree.setBackgroundResource(R.drawable.yuanjiao_gray);
//                linearLayout.setVisibility(View.INVISIBLE);
                break;
            case Config.LEAVECHECKDETAIL:   // 审批详情
                dialog.dismiss();
                LogUtil.e("审批详情返回数据");
                JSONObject checkData = (JSONObject) event.getData();
                if (checkData != null) {
                    JSONObject data1 = checkData.optJSONObject("data");
                    if (data1 != null) {
                        name.setText("申请人：" + data1.optString("nickName"));
                        GlideUtil.loadImageHead(data1.optString("photoUrl"),image);
                        String ema = "";
                        if (data1.optInt("ema") == 1) {
                            ema = "上午";
                        }else{
                            ema = "下午";
                        }
                        String sma = "";
                        if (data1.optInt("sma") == 1) {
                            sma = "上午";
                        }else{
                            sma = "下午";
                        }
                        oldApplyTime = data1.optString("applyerTime");
                        applyTime.setText(data1.optString("applyerTime").substring(0,11));
                        if (1 == data1.optInt("type")){
                            leaveType.setText("婚假");
                        }else if(2 == data1.optInt("type")){
                            leaveType.setText("病假");
                        }else if(3 == data1.optInt("type")){
                            leaveType.setText("事假");
                        }
                        leaveStartTime.setText(data1.optString("startTime").substring(0,11) + sma);
                        leaveEndTime.setText(data1.optString("endTime").substring(0,11) + ema);
                        leaveReason.setText(data1.optString("summary"));

                        JSONArray taskList = data1.optJSONArray("taskList");
                        if (taskList != null && taskList.length() > 0) {
                            for (int i = 0; i < taskList.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) taskList.get(i);
                                    RepairsProposerState mRepairsProposerState = new RepairsProposerState();
                                    mRepairsProposerState.state = o.optString("nodeName");
                                    mRepairsProposerState.image = o.optString("photoUrl");
                                    mRepairsProposerState.name = o.optString("nickName");
                                    mRepairsProposerState.content = o.optString("content");
                                    mRepairsProposerState.time = o.optString("flowTime");
                                    if (!"0".equals(o.optString("status"))) {
                                        lists.add(mRepairsProposerState);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case Config.LEAVEDETAIL:  // 请假详情
                dialog.dismiss();
                LogUtil.e("请假详情返回数据");
                JSONObject data = (JSONObject) event.getData();
                if (data != null) {
                    JSONObject data1 = data.optJSONObject("data");
                    if (data1 != null) {

                        String ema = "";
                        if (data1.optInt("ema") == 1) {
                            ema = "上午";
                        }else{
                            ema = "下午";
                        }
                        String sma = "";
                        if (data1.optInt("sma") == 1) {
                            sma = "上午";
                        }else{
                            sma = "下午";
                        }

                        name.setText("申请人：" + data1.optString("nickName"));
                        GlideUtil.loadImageHead(data1.optString("photoUrl"),image);
                        applyerTeacherId = data1.optString("applyerTeacherId");
                        oldApplyTime = data1.optString("createTime");
                        applyTime.setText(data1.optString("createTime").substring(0,11));
                        startLeaveTime = data1.optString("createTime").substring(0,11);
                        JSONArray taskList = data1.optJSONArray("taskList");
                        if (taskList != null && taskList.length() > 0) {
                            for (int i = 0; i < taskList.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) taskList.get(i);
                                    RepairsProposerState mRepairsProposerState = new RepairsProposerState();
                                    mRepairsProposerState.state = o.optString("nodeName");
                                    mRepairsProposerState.image = o.optString("photoUrl");
                                    mRepairsProposerState.name = o.optString("nickName");
                                    mRepairsProposerState.content = o.optString("content");
                                    mRepairsProposerState.time = o.optString("flowTime");

                                    if (!"0".equals(o.optString("status"))) {
                                        lists.add(mRepairsProposerState);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                NetworkRequest.request(new LeaveCheckPageRequest("1","20",oid), CommonUrl.LEAVE_TIPSAY_CHECK_PAGE,Config.LEAVE_TIPSAY_CHECK_PAGE_MODIFY);  // 分页
                break;
            case Config.MODIFYREJECTAPPLY:  // 修改请假
                ToastUtils.toast("修改成功");
                LogUtil.e("驳回请假再次提出的申请参数",GsonUtils.toJson(new AgreeRequest(oid,"")));
                NetworkRequest.request(new AgreeRequest(oid,""), CommonUrl.REJECTAPPLY, Config.REJECTAPPLY);
                finish();
                break;
            case Config.REJECTAPPLY:
                dialog.dismiss();
                ToastUtils.toast("申请成功");
                break;
            case Config.LEAVE_TIPSAY_CHECK_PAGE_MODIFY:
                JSONObject tipsay_page = (JSONObject) event.getData();
                LogUtil.e("排课分页数据 = ",tipsay_page.toString());
                if (isCheck){ //请假详情,"+++++");
                    if (tipsay_page != null) {
                        dialog.dismiss();
                        JSONArray data1 = tipsay_page.optJSONArray("data");
                        if (data1 != null && data1.length() > 0) {
                            List<Map<String,List<LeaveStateScheduleBean>>>  mapList = new ArrayList<>();
                            List<String> dates = new ArrayList<>();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar start = Calendar.getInstance();
                            Calendar currentDate = Calendar.getInstance();
                            for (int i = 0; i < data1.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) data1.get(i);
                                    if (o != null) {
                                        if (!TextUtils.isEmpty(o.optString("linkTime"))) {
                                            LogUtil.e("请假详情的日期 = ",o.optString("linkTime"));
                                            String date = o.optString("linkTime").split(" ")[0];
                                            if (!dates.contains(date)){
                                                try {
                                                    start.setTime(format.parse(startLeaveTime));
                                                    currentDate.setTime(format.parse(date));
                                                    //比较当前日期是否属于开始日期之前的日期
                                                    int result = start.compareTo(currentDate);
                                                    if (result <= 0){
                                                        Map<String,List<LeaveStateScheduleBean>> map = new HashMap<>();
                                                        List<LeaveStateScheduleBean> beanList1 = new ArrayList<>();
                                                        LeaveStateScheduleBean mLeaveStateScheduleBean = new LeaveStateScheduleBean();
                                                        mLeaveStateScheduleBean.clazz = o.optString("clazz");
                                                        mLeaveStateScheduleBean.subject = o.optString("subjectName");
                                                        mLeaveStateScheduleBean.className = o.optString("className");
                                                        mLeaveStateScheduleBean.tipsay = o.optString("oname");
                                                        beanList1.add(mLeaveStateScheduleBean);
                                                        map.put(date,beanList1);
                                                        dates.add(date);
                                                        mapList.add(map);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }else {
                                                for (int h = 0 ; h < mapList.size() ; h++){
                                                    Map<String,List<LeaveStateScheduleBean>> map = mapList.get(h);
                                                    Set<String> keys = map.keySet();
                                                    for (String key : keys){
                                                        if(key.equals(date)){
                                                            List<LeaveStateScheduleBean> list = map.get(key);
                                                            LeaveStateScheduleBean mLeaveStateScheduleBean = new LeaveStateScheduleBean();
                                                            mLeaveStateScheduleBean.clazz = o.optString("clazz");
                                                            mLeaveStateScheduleBean.subject = o.optString("subjectName");
                                                            mLeaveStateScheduleBean.className = o.optString("className");
                                                            mLeaveStateScheduleBean.tipsay = o.optString("oname");
                                                            list.add(mLeaveStateScheduleBean);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (mapList.size() > 0 ){
                                for (int i = 0 ; i < mapList.size() ; i ++){
                                    Map<String,List<LeaveStateScheduleBean>> listMap = mapList.get(i);
                                    Set<String> keys = listMap.keySet();
                                    for (String key : keys){
                                        LeaveStateSchedule mLeaveStateSchedule = new LeaveStateSchedule();
                                        mLeaveStateSchedule.date = key;
                                        mLeaveStateSchedule.lists.addAll(listMap.get(key));
                                        delegate.lists.add(mLeaveStateSchedule);
                                    }
                                }
                            }
                        }
                    }
                    delegate.initList(1); // 2不可选择添加代课老师
                    setPkListView();
                }else { //审批"请假详情"
//                    beanList = GsonUtils.toArray(LeaveStateBean.class,tipsay_page);
//                    requestSchedule(applyerTeacherId);
                }
                break;
            case Config.LEAVE_ID_SCHEDULE:   // TODO 排课表
                dialog.dismiss();
//                delegate.lists.clear();
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
                                                mLeaveStateScheduleBean.linkId = o1.optString("linkId");
                                                mLeaveStateScheduleBean.lteacherId = o1.optString("lteacherId");
                                                mLeaveStateScheduleBean.oteacherId = o1.optString("oteacherId");

                                                boolean hasDate = false;
                                                boolean isReplace = false;
                                                int length = delegate.lists.size();
                                                int k = 0, m = 0;
                                                if(delegate.lists.size() > 0) {
                                                    for (k = 0; k < length; k++) {
                                                        if (delegate.lists.get(k).date.equals(mLeaveStateSchedule.date)) {
                                                            hasDate = true;
                                                            break;
                                                        }
                                                    }
                                                    if (hasDate && delegate.lists.get(k).lists != null) {
                                                        for (m = 0; m < delegate.lists.get(k).lists.size(); m++) {
                                                            if (delegate.lists.get(k).lists.get(m).clazz != null
                                                                    && delegate.lists.get(k).lists.get(m).clazz.equals(mLeaveStateScheduleBean.clazz)) {
                                                                isReplace = true;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if(!isReplace)
                                                    mLeaveStateSchedule.lists.add(mLeaveStateScheduleBean);
                                                else
                                                    mLeaveStateSchedule.lists.add(delegate.lists.get(k).lists.get(m));

//                                                String schoolId =mLeaveStateScheduleBean .schoolId;
//                                                String teacherId =mLeaveStateScheduleBean.teacherId;
//                                                String week = mLeaveStateScheduleBean.week;
//                                                String clazz = mLeaveStateScheduleBean.clazz;
//                                                String subjectId =mLeaveStateScheduleBean.subjectId;
//                                                String classId =mLeaveStateScheduleBean.classId;
//                                                String linkId =mLeaveStateScheduleBean.linkId;
//                                                String linkTime = null;
//                                                if(delegate.lists.size() > 0)
//                                                    linkTime = delegate.lists.get(delegate.mGroupPosition).date;
//                                                String oteacherId = mLeaveStateScheduleBean.oteacherId;
//                                                LeaveTipsayAddRequest mLeaveTipsayAddRequest = new LeaveTipsayAddRequest(linkTime,classId,schoolId,oid,teacherId,week,clazz,subjectId,oteacherId,linkId);
//
//                                                tipsayAddRequestList.add(mLeaveTipsayAddRequest);
                                            }
                                        }
                                    }
//                                    if(delegate.lists.size() == 0){
//                                        delegate.lists.add(mLeaveStateSchedule);
//                                    }else {
//                                        boolean isReplace = false;
                                        int length = delegate.lists.size();
                                        for (int j = 0; j < length; j++) {
                                            if (delegate.lists.get(j).date.equals(mLeaveStateSchedule.date)) {
//                                                isReplace = true;
                                                delegate.lists.remove(j);
                                                break;
                                            }
                                        }
                                        delegate.lists.add(mLeaveStateSchedule);
//                                        int before = DateUtil.compareDate(mLeaveStateSchedule.date, delegate.lists.get(0).date);
//                                        int after = DateUtil.compareDate(mLeaveStateSchedule.date, delegate.lists.get(length-1).date);
////                                        if (!isReplace) {
//                                            if (before == -1)
//                                                delegate.lists.add(0, mLeaveStateSchedule);
//                                            else if (after == 1)
//                                                delegate.lists.add(mLeaveStateSchedule);
////                                        }
//                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    updatePkList();
                }
                break;
            case Config.TEACHERFLY:
                // 代课老师名称
                Map<String,String> eventData = (Map<String,String>) event.getData();
                String nameTipsay = eventData.get("value");
                int groupChild = Integer.parseInt(eventData.get("position"));

                delegate.lists.get(delegate.mGroupPosition).lists.get(delegate.mChildPosition).tipsay = nameTipsay;
                delegate.initList(1); // 刷新适配器

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

                LeaveTipsayAddRequest mLeaveTipsayAddRequest = new LeaveTipsayAddRequest(linkTime,classId,schoolId,oid,teacherId,week,clazz,subjectId,tipsayTeacherId,linkId);
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
            case Config.SUPPLYTEACHER:
                dialog.dismiss();
                JSONObject schedule1 = (JSONObject) event.getData();
                if (schedule1 != null) {
                    tipsayBeans.clear();
                    tipsMaps.clear();
                    List<TipsayBean> beans = GsonUtils.toArray(TipsayBean.class, schedule1);
                    if (beans != null) {
                        tipsayBeans.addAll(beans);
                        for (TipsayBean bean1 : beans) {
                            tipsMaps.put(bean1.getName(), bean1.getTeacherId());
                        }
                    }

                }
                SelectTeacherDialog selectTeacherDialog = new SelectTeacherDialog(this, -1, tipsayBeans);  // 代课老师对话框
                selectTeacherDialog.setGroupChild(-1);
                break;
            case "leave_add_schedule_status":
                try {
                    JSONObject jb = ((JSONObject) event.getData()).getJSONObject("data").getJSONObject("entity");
                    status = jb.optInt("status");

//                    if (status == 1) {
//                        kcapLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        kcapLayout.setVisibility(View.GONE);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "LEAVE_TIPSAY_ADD_LIST":
//                try {
//                    JSONObject data1 = ((JSONObject)event.getData()).getJSONObject("data");
//                    String linkIds = data1.optString("linkIds");
//                    LeaveApplyForContentModifyRequest leaveApplyForContentModifyRequest = new LeaveApplyForContentModifyRequest(oid, "1", "1", oldApplyTime, sTime, startFlag, eTime, endFlag, requestType, leaveReason.getText().toString().trim(), "1", "1", "1", "1", linkIds);
//                    LogUtil.e("修改请假申请内容", GsonUtils.toJson(leaveApplyForContentModifyRequest));
//                    NetworkRequest.request(leaveApplyForContentModifyRequest, CommonUrl.MODIFYREJECTAPPLY, Config.MODIFYREJECTAPPLY);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                LeaveApplyForContentModifyRequest leaveApplyForContentModifyRequest = new LeaveApplyForContentModifyRequest(oid, "1", "1", oldApplyTime, sTime, startFlag, eTime, endFlag, requestType, leaveReason.getText().toString().trim(), "1", "1", "1", "1");
                LogUtil.e("修改请假申请内容", GsonUtils.toJson(leaveApplyForContentModifyRequest));
                NetworkRequest.request(leaveApplyForContentModifyRequest, CommonUrl.MODIFYREJECTAPPLY, Config.MODIFYREJECTAPPLY);
                break;
        }
    }

    private void updatePkList(){
        int length = delegate.lists.size();
        for(int i = 0; i < length; i++){
            int before = DateUtil.compareDate(delegate.lists.get(i).date, leaveStartTime.getText().toString().substring(0, 11));
            int after = DateUtil.compareDate(delegate.lists.get(i).date, leaveEndTime.getText().toString().substring(0, 11));
            if(before == -1 || after == 1){
                delegate.lists.get(i).isVisible = false;
            }else {
                delegate.lists.get(i).isVisible = true;
            }
        }

//        for(int j = length - 1; j >= 0; j--) {
//            int after = DateUtil.compareDate(delegate.lists.get(j).date, leaveEndTime.getText().toString().substring(0, 11));
//            if(after == 1){
//                delegate.lists.get(j).isVisible = false;
//            }
//        }

        delegate.initList(1);
    }

    @OnClick({R.id.title_back_img,R.id.right_text,R.id.activity_leave_apply_for_state_agree,R.id.activity_leave_apply_for_state_disagree,R.id.activity_leave_apply_for_start_time_reject,R.id.activity_leave_apply_for_end_time_reject,R.id.activity_leave_apply_for_type_reject,R.id.activity_leave_apply_for_image})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.activity_leave_apply_for_start_time_reject:  // 开始时间
                isTime = true;
                mWheelUtil.selectData(LeaveApplyForStateRejectActivity.this);
                break;
            case R.id.activity_leave_apply_for_end_time_reject:  // 结束时间
                isTime = false;
                mWheelUtil.selectData(LeaveApplyForStateRejectActivity.this);
                break;
            case R.id.activity_leave_apply_for_image:  // 类型
                BaseUtil.selectSubject(leaves, this, leaveType);
                break;
            case R.id.right_text:  // TODO 重新提交
                requestType = maps.get(leaveType.getText().toString());
                if (TextUtils.isEmpty(requestType) || TextUtils.isEmpty(sTime) || TextUtils.isEmpty(eTime)) {
                    ToastUtils.toast("请假类型或者时间不能为空");
                    break;
                }
                if (startInt > endInt) {
                    ToastUtils.toast("起始时间不能大于截止时间");
                    break;
                }

                dialog.show();
                if (status == 1 && tipsayAddRequestList.size() > 0) {
                    Map<String, String> map = new HashMap<>();
                    map.put("oid", oid);
                    map.put("useId", SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey));
                    map.put("schoolId", SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey));
                    map.put("oaLinkedParam", GsonUtils.toJson(tipsayAddRequestList));
                    NetworkRequest.request(map, CommonUrl.LEAVE_TIPSAY_ADD_LIST, "LEAVE_TIPSAY_ADD_LIST");
                } else{
                    LeaveApplyForContentModifyRequest leaveApplyForContentModifyRequest = new LeaveApplyForContentModifyRequest(oid, "1", "1", oldApplyTime, sTime, startFlag, eTime, endFlag, requestType, leaveReason.getText().toString().trim(), "1", "1", "1", "1");
                    LogUtil.e("修改请假申请内容", GsonUtils.toJson(leaveApplyForContentModifyRequest));
                    NetworkRequest.request(leaveApplyForContentModifyRequest, CommonUrl.MODIFYREJECTAPPLY, Config.MODIFYREJECTAPPLY);
                }
//                if (!TextUtils.isEmpty(requestType) && !TextUtils.isEmpty(sTime) && !TextUtils.isEmpty(eTime)) {
//                    if (startInt < endInt) {
//                        dialog.show();
//                        LeaveApplyForContentModifyRequest leaveApplyForContentModifyRequest = new LeaveApplyForContentModifyRequest(oid,"1","1",oldApplyTime,sTime,startFlag,eTime,endFlag,requestType,leaveReason.getText().toString().trim(),"1","1","1","1");
//                        LogUtil.e("修改请假申请内容", GsonUtils.toJson(leaveApplyForContentModifyRequest));
//                        NetworkRequest.request(leaveApplyForContentModifyRequest, CommonUrl.MODIFYREJECTAPPLY,Config.MODIFYREJECTAPPLY);
//                    }else if(startInt == endInt){
//                        if (pm >= am) {
//                            dialog.show();
//                            LeaveApplyForContentModifyRequest leaveApplyForContentModifyRequest = new LeaveApplyForContentModifyRequest(oid,"1","1",oldApplyTime,sTime,startFlag,eTime,endFlag,requestType,leaveReason.getText().toString().trim(),"1","1","1","1");
//                            LogUtil.e("修改请假申请内容", GsonUtils.toJson(leaveApplyForContentModifyRequest));
//                            NetworkRequest.request(leaveApplyForContentModifyRequest, CommonUrl.MODIFYREJECTAPPLY,Config.MODIFYREJECTAPPLY);
//                        }else{
//                            ToastUtils.toast("起始时间不能大于截止时间");
//                        }
//                    }else{
//                        ToastUtils.toast("起始时间不能大于截止时间");
//                    }
//                }else{
//                    ToastUtils.toast("请假类型或者时间不能为空");
//                }
                break;
//            case R.id.activity_leave_apply_for_state_agree:
//                new DialogLeaveState(this,"1",oid,dialog);
//                break;
//            case R.id.activity_leave_apply_for_state_disagree:
//                new DialogLeaveState(this,"-1",oid,dialog);
//                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegate = null;
    }

    @Override
    public void onClickSubmit() {
        LogUtil.e("请假内容",mWheelUtil.historyDate);
        if (isTime) {
            timeStart = mWheelUtil.historyDate;
            String start = timeStart.substring(0,10).trim().replace("-","");
            startInt = Integer.parseInt(start);
            startFlag = (Integer.parseInt(mWheelUtil.flag) + 1) + "";
            sTime = mWheelUtil.historyDate;

            if (mWheelUtil.flag != null) {
                am = Integer.parseInt(mWheelUtil.flag);
                if (Integer.parseInt(mWheelUtil.flag) == 0) {
                    leaveStartTime.setText(mWheelUtil.historyDate + " 上午");
                }else {
                    leaveStartTime.setText(mWheelUtil.historyDate + " 下午");
                }
            }
        }else {
            timeEnd = mWheelUtil.historyDate;
            String end = timeEnd.substring(0,10).trim().replace("-","");
            endInt = Integer.parseInt(end);
            endFlag = (Integer.parseInt(mWheelUtil.flag) + 1) + "";
            eTime = mWheelUtil.historyDate;

            if (mWheelUtil.flag != null) {
                pm = Integer.parseInt(mWheelUtil.flag);
                if (Integer.parseInt(mWheelUtil.flag) == 0) {
                    leaveEndTime.setText(mWheelUtil.historyDate + " 上午");
                }else {
                    leaveEndTime.setText(mWheelUtil.historyDate + " 下午");
                }
            }
        }
    }
}
