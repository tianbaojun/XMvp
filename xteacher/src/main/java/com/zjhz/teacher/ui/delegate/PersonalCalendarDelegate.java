package com.zjhz.teacher.ui.delegate;

import android.view.View;
import android.widget.TextView;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarAllRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.PersonalCalendarActivity;
import com.zjhz.teacher.ui.view.PullToRefreshView;
import com.zjhz.teacher.ui.view.calendar.MyMonthView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 个人行事历
 */
public class PersonalCalendarDelegate implements MyMonthView.OnScrollListener,View.OnClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private PersonalCalendarActivity activity;
    private OnDateClicklistener mOnDateClicklistener;
    private List<View> views = new ArrayList<>();
    public PersonalCalendarDelegate(PersonalCalendarActivity activity) {
        this.activity = activity;
    }

    public void initialize(OnDateClicklistener mOnDateClicklistener) {
        this.mOnDateClicklistener = mOnDateClicklistener;
        initView();
        initData();
    }

    private String day;
    public List<String> list = new ArrayList<>();  // TODO 显示事务点的日期
    private void initData() {
        if (activity.myMonthView.getmSelDayTwo() != 0) {
            activity.monthDay.setText(activity.myMonthView.getmSelMonthOne()+ "月" + activity.myMonthView.getmSelDayTwo() + "日");
        }

        activity.myMonthView.setOnScrollListener(this);
        activity.titleTv.setText(activity.getResources().getText(R.string.personal_calendar));
        activity.yearMonth.setText(activity.myMonthView.getmSelYear()+ "年" + activity.myMonthView.getmSelMonthOne()+ "月");

        activity.myMonthView.setDateClick(new MyMonthView.DateClick() {

            @Override
            public void onClickOnDate() {
                day = activity.myMonthView.getmSelYear()+ "-" + activity.myMonthView.getmSelMonth() + "-" + activity.myMonthView.getmSelDayOne();
                String time = activity.myMonthView.getmSelDay();
                mOnDateClicklistener.OnDateClicklistener(day,time);
//                ToastUtils.toast("-----------------平平仄仄平------------------");
                if (activity.myMonthView.getmSelDayTwo() != 0) {
                    activity.monthDay.setText(activity.myMonthView.getmSelMonthOne()+ "月" + activity.myMonthView.getmSelDayTwo() + "日");
                }
            }
        });
    }

    private void initView() {
        activity.mPullToRefreshView.setOnHeaderRefreshListener(this);
        activity.mPullToRefreshView.setOnFooterRefreshListener(this);
        activity.mPullToRefreshView.setLastUpdated(new Date().toLocaleString());
        views.add(activity.one);
        views.add(activity.two);
        views.add(activity.three);
        views.add(activity.four);
        views.add(activity.five);
        views.add(activity.six);
        views.add(activity.seven);
        activity.one.setOnClickListener(this);
        activity.two.setOnClickListener(this);
        activity.three.setOnClickListener(this);
        activity.four.setOnClickListener(this);
        activity.five.setOnClickListener(this);
        activity.six.setOnClickListener(this);
        activity.seven.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_personal_calendar_one:
                setViewBackGround(0);
                break;
            case R.id.activity_personal_calendar_two:
                setViewBackGround(1);
                break;
            case R.id.activity_personal_calendar_three:
                setViewBackGround(2);
                break;
            case R.id.activity_personal_calendar_four:
                setViewBackGround(3);
                break;
            case R.id.activity_personal_calendar_five:
                setViewBackGround(4);
                break;
            case R.id.activity_personal_calendar_six:
                setViewBackGround(5);
                break;
            case R.id.activity_personal_calendar_seven:
                setViewBackGround(6);
                break;
        }
    }

    private void setViewBackGround(int position){
        for (int i = 0; i < views.size() ; i++) {
            if (i == position) {
                views.get(position).setBackgroundResource(R.drawable.circle);
                TextView view = (TextView) views.get(position);
                view.setTextColor(activity.getResources().getColor(R.color.white));
            }else{
                views.get(i).setBackgroundResource(R.drawable.circle_white);
                TextView view = (TextView) views.get(i);
                view.setTextColor(activity.getResources().getColor(R.color.black));
            }
        }
    }

    @Override
    public void setOnScrollListener() {   // 左右滑动监听事件
        activity.yearMonth.setText(activity.myMonthView.getmSelYear()+ "年" + activity.myMonthView.getmSelMonthOne()+ "月");

        if (activity.myMonthView.getmSelDayTwo() != 0) {
            activity.monthDay.setText(activity.myMonthView.getmSelMonthOne()+ "月" + activity.myMonthView.getmSelDayTwo() + "日");
            SharedPreferencesUtils.putSharePrefInteger("date_fff",activity.myMonthView.getmSelDayTwo());
            activity.lists.clear();
            activity.dutys.clear();
            activity.leaders.clear();
            activity.listView.requestLayout();
            activity.adapter.notifyDataSetChanged();
            activity.dialog.show();
            PersonalCalendarAllRequest mPersonalCalendarRequest = new PersonalCalendarAllRequest(activity.myMonthView.getmSelDay());
            NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONCALENDEREVENT, Config.PERSONCALENDEREVENTFLY);
            activity.dialog.show();
            String date = activity.myMonthView.getmSelYear() + "-" + activity.myMonthView.getmSelMonth() + "-" + activity.myMonthView.getmSelDayOne();
            PersonalCalendarRequest scroll = new PersonalCalendarRequest(date);
            LogUtil.e("当天日期的参数", GsonUtils.toJson(scroll));
            NetworkRequest.request(scroll, CommonUrl.PERSONEVENT, Config.PERSONEVENT);
        }else{
            activity.monthDay.setText(activity.myMonthView.getmSelMonthOne()+ "月" + SharedPreferencesUtils.getSharePrefInteger("date_fff") + "日");
        }
    }

    @Override
    public void setOnClickCustomListener(int number) {

    }

    public void setVisible(){
        if (activity.lists.size() <= 0) {
            activity.listViewText.setVisibility(View.VISIBLE);   // 显示背景图片
        }else{
            activity.listViewText.setVisibility(View.GONE);
        }
    }

    public interface OnDateClicklistener{
        void OnDateClicklistener(String date,String time);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        activity.mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                activity.mPullToRefreshView.onFooterRefreshComplete();
//                lists.clear();
//                PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest("2016-06-13");
//                NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENT);
//                delegate.setVisible();
//                adapter.notifyDataSetChanged();
//                ToastUtils.toast("加载更多数据!");
            }
        }, 500);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        activity.mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.mPullToRefreshView.onHeaderRefreshComplete("更新于:" + Calendar.getInstance().getTime().toLocaleString());
                activity.mPullToRefreshView.onHeaderRefreshComplete();
//                ToastUtils.toast("数据刷新完成!");
//                lists.clear();
//                delegate.setVisible();
            }
        }, 500);
    }
}
