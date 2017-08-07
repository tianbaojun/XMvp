package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SchoolCalendarManagerEventItem;
import com.zjhz.teacher.NetworkRequests.request.SchoolCalendarManagerTime;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.SchoolCalendarManagerAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.delegate.SchoolCalendarManagerDelagate;
import com.zjhz.teacher.ui.view.PullToRefreshView;
import com.zjhz.teacher.ui.view.calendar.MonthView;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 校历管理
 */
public class SchoolCalendarManagerActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener {
    private final static String TAG = SchoolCalendarManagerActivity.class.getSimpleName();
    @BindView(R.id.title_tv)
    public TextView titleTv;
    @BindView(R.id.title_back_img)
    public TextView title_back_img;
    @BindView(R.id.activity_school_calendar_manager_month)
    public TextView month;
    @BindView(R.id.activity_school_calendar_manager_one)
    public TextView one;
    @BindView(R.id.activity_school_calendar_manager_two)
    public TextView two;
    @BindView(R.id.activity_school_calendar_manager_three)
    public TextView three;
    @BindView(R.id.activity_school_calendar_manager_four)
    public TextView four;
    @BindView(R.id.activity_school_calendar_manager_five)
    public TextView five;
    @BindView(R.id.activity_school_calendar_manager_six)
    public TextView six;
    @BindView(R.id.activity_school_calendar_manager_monthDateView)
    public MonthView myMonthView;

    @BindView(R.id.activity_school_calendar_manager__pull_refresh_view)
    public PullToRefreshView mPullToRefreshView;
    @BindView(R.id.activity_school_calendar_manager__list_view)
    public ListView listView;
    SchoolCalendarManagerDelagate delagate;
    public SchoolCalendarManagerAdapter adapter;
    public List<String> lists = new ArrayList<>();
    public int currentPager = 1;
    public boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_calendar_manager);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        delagate = new SchoolCalendarManagerDelagate(this);
        delagate.initView();
        initData();/*
        delagate = new SchoolCalendarManagerDelagate(this);
        delagate.initialize();*/
    }

    private void initData() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(new Date().toLocaleString());
        titleTv.setText(getResources().getText(R.string.school_calendar_manager));
        adapter = new SchoolCalendarManagerAdapter(this,lists);
        listView.setAdapter(adapter);

        dialog.show();
        SchoolCalendarManagerTime mSchoolCalendarManagerTime = new SchoolCalendarManagerTime("SchoolCalendarService.currentCalendar");
        NetworkRequest.request(mSchoolCalendarManagerTime, CommonUrl.SCHOOLCALENDARTIME, Config.SCHOOLCALENDARTIME);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delagate = null;
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mPullToRefreshView.onHeaderRefreshComplete("更新于:" + Calendar.getInstance().getTime().toLocaleString());
                mPullToRefreshView.onHeaderRefreshComplete();
                LogUtil.e(TAG,"数据刷新完成!");
            }

        }, 1000);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();
                LogUtil.e(TAG,"加载更多数据!");
                ++currentPager;
            }
        }, 1000);
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
            case Config.SCHOOLCALENDARTIME:  // 当前有效校历的配置信息
                dialog.dismiss();
                isFirst = true;
                JSONObject data = (JSONObject) event.getData();
                if (data != null) {
                    JSONObject data1 = data.optJSONObject("data");
                    if (data1 != null&&!data1.toString().equals("{}")) {
                        String startTime = data1.optString("startTime");
                        String endTime = data1.optString("endTime");
                        String startTimeOne = startTime.substring(0, 11);
                        String endTimeOne = endTime.substring(0,11);
                        String subStartTime = startTime.substring(0, 7);
                        String subEndTime = endTime.substring(0, 7).trim();

                        LogUtil.e(TAG,"起始时间 = " + subStartTime + "   " + "结束时间 = " + subEndTime);
                        SharedPreferencesUtils.putSharePrefString("school_starttimeone",startTimeOne);
                        SharedPreferencesUtils.putSharePrefString("school_endtimeone",endTimeOne);

                        int result = 0;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        Calendar c1 = Calendar.getInstance();
                        Calendar c2 = Calendar.getInstance();
                        try {
                        c1.setTime(sdf.parse(startTimeOne));
                        c2.setTime(sdf.parse(endTimeOne));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

                        int spaceMonth = (result == 0 ? 1 : Math.abs(result));

                        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date sDate = null;
                        Date eDate = null;
                        try {
                             sDate = dateFormat1.parse(startTimeOne);
                             eDate = dateFormat1.parse(endTimeOne);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int startW = sDate.getDay();
                        int endW = eDate.getDay();
                        long diff = Math.abs(eDate.getTime()-sDate.getTime())/1000/60/60/24;
                        int weeks = (int)Math.ceil((diff - (7-startW) - (endW + 1))/7 + 2);
                        //获取时间区间内的总周数和月数
                        SharedPreferencesUtils.putSharePrefInteger("total_weeknum",weeks);
                        SharedPreferencesUtils.putSharePrefInteger("space_month",spaceMonth);

                        SharedPreferencesUtils.putSharePrefString("school_substarttime",subStartTime);  // 起始时间
                        SharedPreferencesUtils.putSharePrefString("school_subendtime",subEndTime);    // 截止时间
                        LogUtil.e(TAG,"周次=== = " + SharedPreferencesUtils.getSharePrefString("school_calendar_week_num"));
                        delagate.initData();
                        SchoolCalendarManagerEventItem mSchoolCalendarManagerEventItem = new SchoolCalendarManagerEventItem(SharedPreferencesUtils.getSharePrefString("school_calendar_week_num"),String.valueOf(currentPager),"80");
                        NetworkRequest.request(mSchoolCalendarManagerEventItem, CommonUrl.SCHOOLEVENTITEM, Config.SCHOOLEVENTITEM);


                    }else{
                        ToastUtils.showShort("网络数据异常");
                    }
                }
                break;
            case Config.SCHOOLEVENTITEM:  // 校历事项列表
                dialog.dismiss();
                lists.clear();
                JSONObject eventlist = (JSONObject) event.getData();
                if (eventlist != null) {
                    JSONArray jsonArray = eventlist.optJSONArray("data");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) jsonArray.get(i);
                                lists.add(o.optString("content"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
//                listView.requestLayout();
                adapter.notifyDataSetChanged();
                isFirst = false;
                break;
        }
    }
}
