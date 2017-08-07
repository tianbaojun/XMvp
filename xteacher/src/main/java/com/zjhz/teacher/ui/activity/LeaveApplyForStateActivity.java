package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.AgreeRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForStateRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveApplyForStateScheduleRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveCheckPageRequest;
import com.zjhz.teacher.NetworkRequests.request.LeaveTipsayAddRequest;
import com.zjhz.teacher.NetworkRequests.response.LeaveStateBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.LeaveApplyStateListAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.LeaveStateSchedule;
import com.zjhz.teacher.bean.LeaveStateScheduleBean;
import com.zjhz.teacher.bean.RepairsProposerState;
import com.zjhz.teacher.bean.TipsayBean;
import com.zjhz.teacher.ui.delegate.LeaveApplyForStateDelegate;
import com.zjhz.teacher.ui.dialog.DialogLeaveState;
import com.zjhz.teacher.ui.dialog.SelectTeacherDialog;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.ScrollAnimatedExpandableListView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

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


import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;import butterknife.BindView;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 请假审批详情
 */
public class LeaveApplyForStateActivity extends BaseActivity {

    private final static String TAG = LeaveApplyForStateActivity.class.getSimpleName();
    @BindView(R.id.activity_leave_apply_for_state_image)
    public CircleImageView image;
    @BindView(R.id.activity_leave_apply_for_state_time)
    public TextView applyTime;
    @BindView(R.id.activity_leave_apply_for_state_type)
    public TextView leaveType;
    @BindView(R.id.activity_leave_apply_for_state_one_time)
    public TextView leaveTime;
    @BindView(R.id.activity_leave_apply_for_state_reason)  // 请假理由
    public TextView leaveReason;
    @BindView(R.id.activity_leave_apply_for_state_expandable_list)
    public ScrollAnimatedExpandableListView expandableListView;
    public String approveFlag;
    public boolean isCheck;
    public String applyerTeacherId;
    public String startTimeShulder;
    public String endTimeShulder;
    public String nameTipsay;
    public boolean isUpdate = false;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.activity_leave_apply_for_state_name)
    TextView name;
    @BindView(R.id.activity_leave_apply_for_state_list)
    ScrollViewWithListView listView;
    List<RepairsProposerState> lists = new ArrayList<>();
    LeaveApplyStateListAdapter adapter;
    LeaveApplyForStateDelegate delegate;
    @BindView(R.id.activity_leave_apply_for_state_agree)
    TextView agree;
    @BindView(R.id.activity_leave_apply_for_state_disagree)
    TextView disagree;
    @BindView(R.id.activity_leave_apply_for_state_linear)
    LinearLayout linearLayout;
    @BindView(R.id.more_ll)
    LinearLayout more_ll;
    @BindView(R.id.revoke_delete_tv)
    TextView rdTv;
    boolean checkFlag;
    List<LeaveStateBean> beanList;
    List<TipsayBean> tipsayBeans = new ArrayList<>();
    Map<String, String> maps = new HashMap<>();
    int more = 0;
    private String oid;
    private int pageNum = 1,pageSize = 9,taskTotal = 0;
    private String startLeaveTime = "";
    private boolean isGet = true;
    private boolean isRefresh = false;
    private int flowStatus = -1;//请假申请单状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply_for_state);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        delegate = new LeaveApplyForStateDelegate(this);
        delegate.initialize();
        initData();
    }

    private void initData() {
        titleTv.setText(R.string.application_for_leave);
        Intent intent = getIntent();
        oid = intent.getStringExtra("leave_list_oid");
        isCheck = intent.getBooleanExtra("leave_list_isCheck",false);
        LogUtil.e("LeaveApplyForStateActivity",isCheck + "");
        dialog.show();
        if (isCheck) {
            LogUtil.e("请假详情","+++++");
            linearLayout.setVisibility(View.GONE);
            NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,pageNum,pageSize), CommonUrl.LEAVEDETAIL,Config.LEAVEDETAIL); // 请假详情
        }else{
            LogUtil.e("审批详情","-----");
            approveFlag = intent.getStringExtra("leave_list_approveFlag");
            LogUtil.e("接收从审批列表传递到审批详情的approveFlag = " + approveFlag);
            if ("1".equals(approveFlag)) { // 可审批
                linearLayout.setVisibility(View.VISIBLE);
                agree.setClickable(true);
                disagree.setClickable(true);
            }else if ("2".equals(approveFlag)) {
//                agree.setClickable(false);
//                agree.setBackgroundResource(R.drawable.yuanjiao_gray);
//                disagree.setClickable(false);
//                disagree.setBackgroundResource(R.drawable.yuanjiao_gray);
                linearLayout.setVisibility(View.GONE);
            }else{
                linearLayout.setVisibility(View.GONE);
            }
            NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,pageNum,pageSize), CommonUrl.LEAVECHECKDETAIL,Config.LEAVECHECKDETAIL);
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
            case Config.AGREE:// ======================================================================================================================================
//                dialog.dismiss();
                ToastUtils.toast("同意");
                approveFlag = "2";
                linearLayout.setVisibility(View.GONE);
                isGet = false;
                isRefresh = true;
                NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,pageNum,pageSize), CommonUrl.LEAVECHECKDETAIL,Config.LEAVECHECKDETAIL);

                Intent intent = new Intent();
                intent.putExtra("isAgree", true);
                setResult(RESULT_OK,intent);

                break;
            case Config.UNAGREE:
//                dialog.dismiss();
                ToastUtils.toast("不同意");
                approveFlag = "2";
                linearLayout.setVisibility(View.GONE);
                isGet = false;
                isRefresh = true;
                NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,pageNum,pageSize), CommonUrl.LEAVECHECKDETAIL,Config.LEAVECHECKDETAIL);

                Intent intent1 = new Intent();
                intent1.putExtra("isDisAgree", true);
                setResult(RESULT_OK,intent1);

                break;
            case Config.LEAVECHECKDETAIL:   // 审批详情
//                dialog.dismiss();
                lists.clear();
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
                        startTimeShulder = data1.optString("startTime");
                        endTimeShulder = data1.optString("endTime");
                        applyerTeacherId = data1.optString("applyerTeacherId");
                        applyTime.setText(data1.optString("applyerTime").substring(0,11));
                        leaveType.setText("请假类型：" + data1.optString("typeVal"));
                        leaveTime.setText(data1.optString("startTime").substring(0,11) + sma + " -- " + data1.optString("endTime").substring(0,11) + ema);
                        leaveReason.setText(data1.optString("summary"));
                        taskTotal = data1.optInt("dtlTotal");
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
                if (lists.size() == 0 ){
                    more_ll.setVisibility(View.GONE);
                }else {
                    if (pageNum * pageSize < taskTotal){
                        more_ll.setVisibility(View.VISIBLE);
                    }else {
                        more_ll.setVisibility(View.GONE);
                    }
                }
                if (more > 0) {
                    more_ll.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                if (isGet){
                    NetworkRequest.request(new LeaveCheckPageRequest("1","20",oid), CommonUrl.LEAVE_TIPSAY_CHECK_PAGE,Config.LEAVE_TIPSAY_CHECK_PAGE);
                }else {
                    dialog.dismiss();
                }
                break;
            case Config.LEAVEDETAIL:  // 请假详情
//                dialog.dismiss();
                lists.clear();
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

                        startTimeShulder = data1.optString("startTime");
                        endTimeShulder = data1.optString("endTime");
                        applyerTeacherId = data1.optString("applyerTeacherId");
                        name.setText("申请人：" + data1.optString("nickName"));
                        GlideUtil.loadImageHead(data1.optString("photoUrl"),image);
                        applyTime.setText(data1.optString("applyerTime").substring(0,11));
                        leaveType.setText("请假类型：" + data1.optString("typeVal"));
                        leaveTime.setText(data1.optString("startTime").substring(0,11) + sma + " -- " + data1.optString("endTime").substring(0,11) + ema);
                        startLeaveTime = data1.optString("startTime").substring(0,11);
                        leaveReason.setText(data1.optString("summary"));
                        taskTotal = data1.optInt("dtlTotal");
                        JSONArray taskList = data1.optJSONArray("taskList");
                        int curNode = 0;
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
                                    curNode = o.optInt("curNode");
                                    if (!"0".equals(o.optString("status"))) {
                                        lists.add(mRepairsProposerState);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        flowStatus = data1.optInt("flowStatus");
                        if(flowStatus == 1 && curNode < 2) {
                            rdTv.setVisibility(View.VISIBLE);
                            rdTv.setText("撤回申请");
                        }
                        if(flowStatus == 3 && curNode < 2) {
                            rdTv.setVisibility(View.VISIBLE);
                            rdTv.setText("删除申请");
                        }
                    }
                }
                if (lists.size() == 0 ){
                    more_ll.setVisibility(View.GONE);
                }else {
                    if (pageNum * pageSize < taskTotal){
                        more_ll.setVisibility(View.VISIBLE);
                    }else {
                        more_ll.setVisibility(View.GONE);
                    }
                }
                if (more > 0) {
                    more_ll.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
//                requestSchedule(applyerTeacherId);
                if (isGet){
                    NetworkRequest.request(new LeaveCheckPageRequest("1","20",oid), CommonUrl.LEAVE_TIPSAY_CHECK_PAGE,Config.LEAVE_TIPSAY_CHECK_PAGE);  // 分页
                }else {
                    dialog.dismiss();
                }
//                NetworkRequest.request(new LeaveDetailRequest(oid), CommonUrl.LEAVE_TIPSAY_CHECK_PAGE,Config.LEAVE_TIPSAY_CHECK_PAGE);  // 详情
                break;
            case Config.LEAVE_ID_SCHEDULE:   // TODO 排课表
                dialog.dismiss();
                delegate.lists.clear();
                JSONObject fly_data = (JSONObject) event.getData();
                LogUtil.e("请假排课表返回数据 = ",fly_data.toString());
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
                                                if (beanList != null && beanList.size() > 0 ){
                                                    for (int h = 0 ; h < beanList.size() ; h ++ ){
                                                        LeaveStateBean bean = beanList.get(h);
                                                        if (mLeaveStateScheduleBean.clazz.equals(bean.getClazz())&&mLeaveStateScheduleBean.week.equals(bean.getWeek())&&
                                                                mLeaveStateSchedule.date.equals(bean.getLinkTime().split(" ")[0])){
                                                            mLeaveStateScheduleBean.tipsay = bean.getOname();
                                                            mLeaveStateScheduleBean.linkId = bean.getLinkId();
                                                        }
                                                    }
                                                }
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
                    delegate.initList(1); // 可以选择代课老师
                }
                break;
            case Config.SUPPLYTEACHER:
                dialog.dismiss();
                JSONObject schedule = (JSONObject) event.getData();
                LogUtil.e("代课老师数据--- = ",schedule.toString());
                if (schedule != null) {
                    tipsayBeans.clear();
                    maps.clear();
                    /*String key = SharedPreferencesUtils.getSharePrefString("fly_tipsay");
                    JSONObject data1 = schedule.optJSONObject("data");
                    if (data1 != null) {
                        JSONArray jsonArray = data1.optJSONArray(key);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) jsonArray.get(i);
                                    if (o != null) {
                                        TipsayBean mTipsayBean = new TipsayBean();
                                        mTipsayBean.name = o.optString("name");
                                        mTipsayBean.nameId = o.optString("teacherId");
                                        tipsayBeans.add(mTipsayBean);
                                        maps.put(o.optString("name"),o.optString("teacherId"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }*/

                    List<TipsayBean> beans = GsonUtils.toArray(TipsayBean.class,schedule);
                    if(beans != null){
                        tipsayBeans.addAll(beans);
                        for(TipsayBean bean : beans) {
                            maps.put(bean.getName(), bean.getTeacherId());
                        }
                    }

                }
                new SelectTeacherDialog(this,-1,tipsayBeans);  // 代课老师对话框
                break;
            case Config.TEACHERFLY:
                // 代课老师名称
                nameTipsay = (String) event.getData();
                delegate.lists.get(delegate.mGroupPosition).lists.get(delegate.mChildPosition).tipsay = nameTipsay;
                delegate.initList(1); // 刷新适配器

                dialog.show();
                LeaveStateScheduleBean bean = delegate.lists.get(delegate.mGroupPosition).lists.get(delegate.mChildPosition);
                String schoolId =bean .schoolId;
                String teacherId =bean.teacherId;
                String week = bean.week;
                String clazz = bean.clazz;
                String subjectId =bean.subjectId;
                String classId =bean.classId;
                String linkId =bean.linkId;
                String linkTime = delegate.lists.get(delegate.mGroupPosition).date;
                String tipsayTeacherId = maps.get(nameTipsay);
                if (isUpdate){
                    LeaveTipsayAddRequest mLeaveTipsayAddRequest = new LeaveTipsayAddRequest(linkId,clazz,week,tipsayTeacherId,subjectId);
                    LogUtil.e("修改代课老师的信息参数 = ", GsonUtils.toJson(mLeaveTipsayAddRequest));
                    NetworkRequest.request(mLeaveTipsayAddRequest, CommonUrl.leave_modify, Config.LEAVE_TIPSAY_ADD);
                }else {
                    LeaveTipsayAddRequest mLeaveTipsayAddRequest = new LeaveTipsayAddRequest(linkTime,classId,schoolId,oid,teacherId,week,clazz,subjectId,tipsayTeacherId);
                    LogUtil.e("添加代课老师的信息参数 = ", GsonUtils.toJson(mLeaveTipsayAddRequest));
                    NetworkRequest.request(mLeaveTipsayAddRequest, CommonUrl.LEAVE_TIPSAY_ADD, Config.LEAVE_TIPSAY_ADD);
                }
                break;
            case Config.LEAVE_TIPSAY_ADD:
                dialog.dismiss();
                if (isUpdate){
                    LogUtil.e("修改代课老师成功");
                }else {
                    LogUtil.e("添加代课老师成功");
                }
//                ToastUtils.toast("添加代课老师成功");
                break;
            case Config.LEAVE_TIPSAY_CHECK_PAGE:
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
                                                        mLeaveStateScheduleBean.knowsStatus = o.optInt("knowsStatus");
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
                                                            mLeaveStateScheduleBean.knowsStatus = o.optInt("knowsStatus");
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
                    delegate.initList(2); // 2不可选择添加代课老师
                }else { //审批"请假详情"
                    beanList = GsonUtils.toArray(LeaveStateBean.class,tipsay_page);
                    requestSchedule(applyerTeacherId);
                }
                break;
            case "leave_revoke":
                ToastUtils.showShort("撤回成功");
                rdTv.setText("删除申请");
//                Intent intent3 = new Intent();
//                intent3.putExtra("revoke", true);
//                setResult(RESULT_OK,intent3);
                isRefresh = true;
                flowStatus = 3;
                break;

            case "leave_delete":
                ToastUtils.showShort("删除成功");
                rdTv.setVisibility(View.GONE);
//                Intent intent4 = new Intent();
//                intent4.putExtra("isDelete", true);
//                setResult(RESULT_OK,intent4);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(isRefresh)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @OnClick({R.id.title_back_img,R.id.activity_leave_apply_for_state_agree,
            R.id.activity_leave_apply_for_state_disagree,R.id.more_ll, R.id.revoke_delete_tv})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_back_img:
                if(isRefresh)
                    setResult(RESULT_OK);
                else
                    setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.activity_leave_apply_for_state_agree:
                DialogLeaveState dialogLeaveState = new DialogLeaveState(this);
                dialogLeaveState.setSureListener(new DialogLeaveState.SureListener() {
                    @Override
                    public void sureClick(String sure) {
                        if (!TextUtils.isEmpty(sure)) {
                            dialog.show();
                            NetworkRequest.request(new AgreeRequest(oid, sure), CommonUrl.AGREE, Config.AGREE);
                        } else {
                            ToastUtils.toast("审批意见不能为空");
                        }
                    }
                });
                break;
            case R.id.activity_leave_apply_for_state_disagree:
                DialogLeaveState dialogLeaveState1 = new DialogLeaveState(this);
                dialogLeaveState1.setSureListener(new DialogLeaveState.SureListener() {
                    @Override
                    public void sureClick(String sure) {
                        if (!TextUtils.isEmpty(sure)) {
                            dialog.show();
                            NetworkRequest.request(new AgreeRequest(oid, sure), CommonUrl.UNAGREE, Config.UNAGREE);
                        } else {
                            ToastUtils.toast("审批意见不能为空");
                        }
                    }
                });
                break;
            case R.id.more_ll:
                if (isCheck){
                    dialog.show();
                    more = 1;
                    isGet = false;
                    NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,pageNum,100), CommonUrl.LEAVEDETAIL,Config.LEAVEDETAIL); // 请假详情
                    more_ll.setVisibility(View.GONE);
                }else {
                    dialog.show();
                    more = 2;
                    isGet = false;
                    NetworkRequest.request(new LeaveApplyForStateRequest(oid,true,pageNum,100), CommonUrl.LEAVECHECKDETAIL,Config.LEAVECHECKDETAIL);
                    more_ll.setVisibility(View.GONE);
                }
                break;
            case R.id.revoke_delete_tv:
                HashMap<String, String> map = new HashMap<>();
                map.put("oid",oid);
//                map.put("desc")
                if(flowStatus == 1)
                    NetworkRequest.request(map, CommonUrl.LEAVE_REVOKE, "leave_revoke");
                if(flowStatus == 3)
                    NetworkRequest.request(map, CommonUrl.LEAVE_DELETE, "leave_delete");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegate = null;
    }

    /**获取排课的接口*/
    private void requestSchedule(String teacherId){
        if(!TextUtils.isEmpty(teacherId)){
            NetworkRequest.request(new LeaveApplyForStateScheduleRequest(teacherId,startTimeShulder,endTimeShulder,1,100), CommonUrl.LEAVE_ID_SCHEDULE,Config.LEAVE_ID_SCHEDULE);
//            NetworkRequest.request(new LeaveApplyForStateScheduleRequest(startTimeShulder,endTimeShulder), CommonUrl.LEAVE_ID_SCHEDULE,Config.LEAVE_ID_SCHEDULE);
        }else{
//            NetworkRequest.request(new LeaveApplyForStateScheduleRequest(startTimeShulder,endTimeShulder), CommonUrl.LEAVE_ID_SCHEDULE,Config.LEAVE_ID_SCHEDULE);
            ToastUtils.toast("教师id不能为空");
        }
    }
}
